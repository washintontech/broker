package com.washintontech.app.model.trade.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TradeModifyRequest(@NotBlank String clientId, @NotNull OrderModifyReq order) {
}
