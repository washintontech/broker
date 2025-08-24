package com.washintontech.app.model.trade;

import quickfix.FieldNotFound;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public record ClientAccount(String clientId, Map<String, Stack<ScriptOrder>> scriptAccountMap) {

    public ClientAccount(String clientId) {
        this(clientId, new HashMap<>());
    }

    public ScriptOrder getActiveScriptOrder(final String scriptId) {
        return scriptAccountMap.get(scriptId)
                .stream()
                .filter(scriptOrder -> scriptOrder.getOrderStatus() == ScriptOrder.OrderStatus.NEW ||
                        scriptOrder.getOrderStatus() == ScriptOrder.OrderStatus.REPLACED)
                .findFirst()
                .orElse(null);
    }

    public ScriptOrder getScriptOrder(final String scriptId, final String clOrderId) {
        return scriptAccountMap.get(scriptId)
                .stream()
                .filter(scriptOrder -> validateClOrderId(clOrderId, scriptOrder))
                .findFirst()
                .orElse(null);
    }

    private static boolean validateClOrderId(final String clOrderId, final ScriptOrder scriptOrder) {
        try {
            return scriptOrder.getNewOrderSingle().getClOrdID().getValue().equals(clOrderId) ||
                    (scriptOrder.getOrderCancelRequest() != null &&
                            scriptOrder.getOrderCancelRequest().getClOrdID().getValue().equals(clOrderId)) ||
                    scriptOrder.getOrderCancelReplaceRequests().peek().getClOrdID().getValue().equals(clOrderId);
        } catch (FieldNotFound e) {
            throw new RuntimeException(e); // Invalid case
        }
    }
}
