package com.washintontech.app.domain;


import com.washintontech.common.Direction;
import com.washintontech.common.Script;
import com.washintontech.exchange.trade.OrderOutboundRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Current Position per orderId
// Modify order = cancel existing order and create new order for same script
@Getter
@Setter
public class OrderState {
    private final String requestId;
    private final String clientId;
    private final String orderId;
    private final Script script;
    private final Direction tradeDirection;
    private final int totalQuantity;
    private final float price;

    private int closedQuantity;
    private float totalTransactionAmount;

    private float averageExecutedPrice;
    private TransactionState transactionState;

    public OrderState(@NotBlank final String requestId, @NotBlank final String clientId, @NotBlank final String orderId,
                      @NotNull final Script script, @NotNull final Direction tradeDirection,
                      @Min(1) final Integer totalQuantity,
                      @DecimalMin(value = "0.01", inclusive = false) final float price) {
        this.requestId = requestId;
        this.clientId = clientId;
        this.orderId = orderId;
        this.script = script;
        this.totalQuantity = totalQuantity;
        this.tradeDirection = tradeDirection;
        this.price = price;
        this.transactionState = TransactionState.ORDER_NEW;
    }

    // TODO : lock
    public void addTransaction(final OrderOutboundRequest orderOutboundRequest) {
        switch (orderOutboundRequest.getExecutionStatus()) {
            case COMPLETED -> {
                closedQuantity = closedQuantity + orderOutboundRequest.getQuantity();
                totalTransactionAmount = totalTransactionAmount + orderOutboundRequest.getOrderExecutionPrice() * orderOutboundRequest.getQuantity();
                averageExecutedPrice = new BigDecimal(Float.toString(totalTransactionAmount / closedQuantity))
                        .setScale(2, RoundingMode.HALF_UP)
                        .floatValue();
                if (closedQuantity == totalQuantity) {
                    transactionState = TransactionState.ORDER_EXECUTED;
                } else {
                    transactionState = TransactionState.ORDER_PARTIALLY_EXECUTED;
                }
            }
            case CANCELED -> {
                if (closedQuantity > 0) {
                    transactionState = TransactionState.ORDER_PARTIALLY_CANCELLED;
                } else {
                    transactionState = TransactionState.ORDER_CANCELLED;
                }
            }
            default -> {
                // log.error
            }
        }
    }

//    public record TransactionRecord(@NotNull Integer quantity,
//                                    @NotNull Float price,
//                                    @AllowedTransaction({TransactionState.ORDER_EXECUTED, TransactionState.ORDER_CANCELLED})
//                                    TransactionState transactionState,
//                                    @NotNull Timestamp timestamp) {
//    }
//
//    void addTransaction(TransactionRecord transactionRecord) {
//        lock.lock();
//        try {
//            switch (transactionRecord.transactionState) {
//                case ORDER_EXECUTED -> {
//                    closedQuantity = closedQuantity + transactionRecord.quantity;
//                    totalTransactionAmount = totalTransactionAmount + transactionRecord.price * transactionRecord.quantity;
//                    averageExecutedPrice = new BigDecimal(Float.toString(totalTransactionAmount / closedQuantity))
//                            .setScale(2, RoundingMode.HALF_UP)
//                            .floatValue();
//                    if (closedQuantity == totalQuantity) {
//                        transactionState = TransactionState.ORDER_EXECUTED;
//                    } else {
//                        transactionState = TransactionState.ORDER_PARTIALLY_EXECUTED;
//                    }
//                }
//                case ORDER_CANCELLED -> {
//                    if (closedQuantity > 0) {
//                        transactionState = TransactionState.ORDER_PARTIALLY_CANCELLED;
//                    } else {
//                        transactionState = TransactionState.ORDER_CANCELLED;
//                    }
//                }
//                default -> {
//                    // log.error
//                }
//            }
//
//
//        } finally {
//            lock.unlock();
//        }
//    }
}
