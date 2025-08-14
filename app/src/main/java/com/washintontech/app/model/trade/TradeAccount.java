package com.washintontech.app.model.trade;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TradeAccount {

    private String clOrderId;
    private String exchangeOrderId;
    private String script;
    private int totalQuantity;
    private int executedQuantity;
    private LocalDate latestTimeStamp;
}
