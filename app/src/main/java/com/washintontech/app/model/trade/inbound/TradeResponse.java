package com.washintontech.app.model.trade.inbound;


import jakarta.validation.constraints.NotBlank;

public record TradeResponse(@NotBlank String requestId) { // TODO: Add more description, especially for error cases.
}
