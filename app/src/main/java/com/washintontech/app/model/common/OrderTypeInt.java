package com.washintontech.app.model.common;

import com.washintontech.common.OrderType;

public enum OrderTypeInt {
    NEW_MARKET,
    NEW_LIMIT,
    CANCEL,
    MODIFY;

    public static OrderType toOrderType(final OrderTypeInt orderTypeInt) {
        return switch (orderTypeInt) {
            case NEW_MARKET -> OrderType.NEW_MARKET;
            case NEW_LIMIT -> OrderType.NEW_LIMIT;
            case CANCEL -> OrderType.CANCEL;
            case MODIFY -> OrderType.MODIFY;
        };
    }
}
