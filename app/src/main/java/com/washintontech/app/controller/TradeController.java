package com.washintontech.app.controller;

import com.washintontech.app.model.trade.inbound.TradeCancelRequest;
import com.washintontech.app.model.trade.inbound.TradeModifyRequest;
import com.washintontech.app.model.trade.inbound.TradeNewRequest;
import com.washintontech.app.model.trade.inbound.TradeResponse;
import com.washintontech.app.service.trade.inbound.TradeComplianceService;
import com.washintontech.app.service.trade.inbound.TradeInboundService;
import com.washintontech.app.service.trade.inbound.TradeInboundValidationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quickfix.FieldNotFound;

@RestController
@RequestMapping(value = "/client/trader/")
public class TradeController {

    private static final Logger log = LogManager.getLogger(TradeController.class);
    private final TradeInboundService tradeInboundService;
    private final TradeInboundValidationService validationService;
    private final TradeComplianceService complianceService;

    public TradeController(final TradeInboundService tradeInboundService,
                           final TradeInboundValidationService validationService,
                           final TradeComplianceService complianceService) {
        this.tradeInboundService = tradeInboundService;
        this.validationService = validationService;
        this.complianceService = complianceService;
    }

    @PostMapping(path = "/place/order", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TradeResponse newTrade(@RequestBody TradeNewRequest tradeNewRequest) {
        log.debug("Received trade request: {}", tradeNewRequest);
        return tradeInboundService.trade(tradeNewRequest);
    }

    @PutMapping(path = "/update/order", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TradeResponse modifyTrade(@RequestBody TradeModifyRequest tradeModifyRequest) throws FieldNotFound {
        log.debug("Received trade request: {}", tradeModifyRequest);
        return tradeInboundService.trade(tradeModifyRequest);
    }

    @PostMapping(path = "/cancel/order", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TradeResponse cancelTrade(@RequestBody TradeCancelRequest tradeCancelRequest) throws FieldNotFound {
        log.debug("Received trade request: {}", tradeCancelRequest);
        return tradeInboundService.trade(tradeCancelRequest);
    }
}
