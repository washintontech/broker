package com.washintontech.app.model.trade.inbound;

import com.washintontech.app.model.trade.inbound.OrderRes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record TradeResponse(@NotBlank String requestId, @NotBlank String ClientId, @NotNull Set<OrderRes> orders) {
}
