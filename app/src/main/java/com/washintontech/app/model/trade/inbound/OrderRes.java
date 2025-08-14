package com.washintontech.app.model.trade.inbound;

import com.washintontech.app.model.common.Status;
import com.washintontech.app.model.common.TradeDirection;
import com.washintontech.broker.trade.BrokerTradeResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record OrderRes(@NotBlank String requestId,
                       @NotBlank String orderId,
                       @NotBlank String script,
                       @NotNull Integer quantity,
                       @NotNull @Pattern(regexp = "^(\\d+)?(\\.\\d{1,2})?$") Float orderPlacedPrice,
                       @NotNull TradeDirection tradeDirection,
                       @NotNull Status status) {

    public static TradeResponse toOrderResponse(final BrokerTradeResponse tradeResponse) {
        final var orderResponse = tradeResponse.getOrderInboundResponse();
        final var orderRes = new OrderRes(tradeResponse.getRequestId(), orderResponse.getOrderId(),
                orderResponse.getScript().toString(),
                orderResponse.getQuantity(), orderResponse.getOrderPlacedPrice(),
                TradeDirection.toTradeDirection(orderResponse.getTradeDirection()),
                Status.toStatus(orderResponse.getOrderStatus()));
        return new TradeResponse("", "", Set.of(orderRes));
    }
}
