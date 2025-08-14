package com.washintontech.app.service.trade.inbound;

import com.washintontech.app.model.trade.inbound.TradeNewRequest;
import com.washintontech.app.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TradeInboundValidationService {

    @Value("${broker.id}")
    private String brokerId;

    private final TradeRepository tradeRepository;

    public TradeInboundValidationService(final TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public void requestValidate(final TradeNewRequest req) {
    }

//    public void responseValidate(final BrokerTradeResponse tradeResponse) {
//        final var orderState = tradeRepository.orderStateByRequestId(tradeResponse.getRequestId());
//        final var orderInboundResponse = tradeResponse.getOrderInboundResponse();
//        if (!brokerId.equals(tradeResponse.getBrokerId()) || orderState == null ||
//                orderInboundResponse.getQuantity() != orderState.getTotalQuantity() ||
//                orderInboundResponse.getTradeDirection() != orderState.getTradeDirection() ||
//                orderInboundResponse.getScript() != orderState.getScript() ||
//                orderInboundResponse.getOrderPlacedPrice() != orderState.getPrice()) {
//            throw new RuntimeException(String.format(
//                    "Invalid Inbound trade response bearing brokerId: %s, requestId: %s, orderResponse: %s",
//                    tradeResponse.getBrokerId(), tradeResponse.getRequestId(), tradeResponse.getOrderInboundResponse()));
//        }
//    }
}
