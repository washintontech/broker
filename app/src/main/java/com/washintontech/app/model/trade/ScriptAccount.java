package com.washintontech.app.model.trade;

import lombok.Data;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Data
public class ScriptAccount {
    private String script;
    private NewOrderSingle newOrderSingle;
    private OrderCancelRequest orderCancelRequest;
    private Stack<OrderCancelReplaceRequest> orderCancelReplaceRequests;
    private Stack<ExecutionReport> executionReports;
    private List<TradeAccount> tradeAccounts;

    public ScriptAccount(final String script, final NewOrderSingle newOrderSingle) {
        this.script = script;
        this.newOrderSingle = newOrderSingle;
        this.orderCancelReplaceRequests = new Stack<>();
        this.executionReports = new Stack<>();
        this.tradeAccounts = new ArrayList<>();
    }

    public int totalQuantity() {
        return tradeAccounts.stream().mapToInt(TradeAccount::getTotalQuantity).sum();
    }

    public int executedQuantity() {
        return tradeAccounts.stream().mapToInt(TradeAccount::getExecutedQuantity).sum();
    }
}
