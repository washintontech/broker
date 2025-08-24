package com.washintontech.app.controller;

import com.washintontech.app.model.trade.inbound.TradeCancelRequest;
import com.washintontech.app.model.trade.inbound.TradeModifyRequest;
import com.washintontech.app.model.trade.inbound.TradeNewRequest;
import com.washintontech.app.model.trade.inbound.TradeResponse;
import com.washintontech.app.service.trade.inbound.TradeInboundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickfix.FieldNotFound;

@RestController
@RequestMapping(value = "/client/trader/")
@RequiredArgsConstructor
@Log4j2
public class TradeController {
    private final TradeInboundService tradeInboundService;

    @PostMapping(path = "/place/order", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TradeResponse> newTrade(@RequestBody TradeNewRequest tradeNewRequest) throws FieldNotFound {
        log.debug("Received trade request: {}", tradeNewRequest);
        final var response = tradeInboundService.trade(tradeNewRequest);
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(response);
    }

    @PatchMapping(path = "/update/order", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TradeResponse> modifyTrade(@RequestBody TradeModifyRequest tradeModifyRequest) throws FieldNotFound {
        log.debug("Received trade request: {}", tradeModifyRequest);
        final var response = tradeInboundService.trade(tradeModifyRequest);
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(response);
    }

    @PostMapping(path = "/cancel/order", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TradeResponse> cancelTrade(@RequestBody TradeCancelRequest tradeCancelRequest) throws FieldNotFound {
        log.debug("Received trade request: {}", tradeCancelRequest);
        final var response = tradeInboundService.trade(tradeCancelRequest);
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(response);
    }
}
