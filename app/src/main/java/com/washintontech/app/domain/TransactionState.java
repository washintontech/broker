package com.washintontech.app.domain;

public enum TransactionState {
    ORDER_NEW,
    ORDER_PLACED,
    ORDER_EXECUTED,
    ORDER_CANCELLED,
    ORDER_PARTIALLY_EXECUTED,
    ORDER_PARTIALLY_CANCELLED
}
