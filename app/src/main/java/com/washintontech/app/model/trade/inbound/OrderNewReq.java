package com.washintontech.app.model.trade.inbound;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.Symbol;

public record OrderNewReq(
        @NotBlank Symbol script,
        @NotNull OrderQty quantity,
        //@Pattern(regexp = "^(\\d+)?(\\.\\d{1,2})?$")
        Price price,
        @NotNull Character sideChar,
        Character timeInForceChar,
        @NotNull Character orderTypeChar
) {
}
//public record OrderReq(@NotBlank String script,
//                       @NotNull Integer quantity,
//                       @Pattern(regexp = "^(\\d+)?(\\.\\d{1,2})?$") float limitPrice,
//                       @Pattern(regexp = "^(\\d+)?(\\.\\d{1,2})?$") float triggerPrice,
//                       @NotNull TradeDirection tradeDirection,
//                       @NotNull OrderTypeInt orderTypeInt,
//                       @Nullable String orderId) {
//}
