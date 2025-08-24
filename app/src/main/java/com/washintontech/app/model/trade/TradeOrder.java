package com.washintontech.app.model.trade;

import java.time.LocalDateTime;


public record TradeOrder(String clOrderId,
                         String exchangeOrderId,
                         String script,
                         char side,
                         double executedQuantity,
                         LocalDateTime orderExecutionTime) {
}
