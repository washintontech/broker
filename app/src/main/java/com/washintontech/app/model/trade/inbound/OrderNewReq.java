package com.washintontech.app.model.trade.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Symbol;

public record OrderNewReq(
        @NotBlank Symbol script,
        @NotNull OrderQty quantity,
        Price price,
        @NotNull Character sideChar,
        Character timeInForceChar,
        @NotNull Character orderTypeChar
) {
}

