package com.washintontech.app.model.trade;

import lombok.Data;
import quickfix.FieldNotFound;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Data
public class ScriptOrder {
    private String script;
    private String latestClientOrderId;
    private NewOrderSingle newOrderSingle;
    private OrderCancelRequest orderCancelRequest;
    private Stack<OrderCancelReplaceRequest> orderCancelReplaceRequests;
    private Stack<ExecutionReport> executionReports;
    private List<TradeOrder> tradeOrders;
    private OrderStatus orderStatus;

    public ScriptOrder(final String script, final NewOrderSingle newOrderSingle) throws FieldNotFound {
        this.script = script;
        this.newOrderSingle = newOrderSingle;
        this.orderCancelReplaceRequests = new Stack<>();
        this.executionReports = new Stack<>();
        this.tradeOrders = new ArrayList<>();
        this.orderStatus = OrderStatus.NEW;
        this.latestClientOrderId = newOrderSingle.getClOrdID().getValue();
    }

    public enum OrderStatus {
        NEW,  // NewOrderSingle
        REPLACED, // OrderCancelReplaceRequest
        CANCEL, // OrderCancelRequest
        REJECTED, // Failed at Validation Stage
        EXECUTED // FILL order
    }
}
