package com.washintontech.app.model.common;

import com.washintontech.common.Direction;
import jakarta.validation.constraints.NotNull;

public enum TradeDirection {
    BUY,
    SELL;

    @NotNull(message = "Unknown Trade Direction")
    public static TradeDirection toTradeDirection(final Direction tradeDirection) {
        return switch (tradeDirection) {
            case BID -> BUY;
            case ASK -> SELL;
            case UNRECOGNIZED -> null;
        };
    }

    public static Direction toTradeDirection(final TradeDirection tradeDirection) {
        return switch (tradeDirection) {
            case BUY -> Direction.BID;
            case SELL -> Direction.ASK;
        };
    }
}
