package com.washintontech.app.model.trade.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import quickfix.field.OrderQty;
import quickfix.field.Symbol;

public record OrderCancelReq(
        @NotBlank Symbol script,
        @NotNull OrderQty quantity,
        @NotNull Character sideChar
) {
}
