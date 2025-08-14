package com.washintontech.app.model.trade.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TradeCancelRequest(@NotBlank String clientId, @NotNull OrderCancelReq order) {
}
