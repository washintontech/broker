package com.washintontech.app.model.trade.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TradeNewRequest(@NotBlank String clientId, @NotNull OrderNewReq order) {
}
