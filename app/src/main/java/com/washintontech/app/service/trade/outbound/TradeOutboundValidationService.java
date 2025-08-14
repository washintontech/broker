package com.washintontech.app.service.trade.outbound;

import com.washintontech.app.repository.TradeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TradeOutboundValidationService {

    @Value("${broker.id}")
    private String brokerId;

    private final TradeRepository tradeRepository;

    public TradeOutboundValidationService(final TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

//    public void validate(final ExchangeTradeRequest exchangeTradeRequest) {
//        final var orderOutboundRequest = exchangeTradeRequest.getOrderOutboundRequest();
//        final var orderState = tradeRepository.orderStateByOrderId(orderOutboundRequest.getOrderId());
//        final var executionStatus = exchangeTradeRequest.getOrderOutboundRequest().getExecutionStatus();
//        if (executionStatus != ExecutionStatus.CANCELED && executionStatus != ExecutionStatus.COMPLETED) {
//            throw new RuntimeException(String.format("Unknown execution status: %s", executionStatus));
//        }
//
//        if (!brokerId.equals(exchangeTradeRequest.getBrokerId()) || orderState == null ||
//                orderOutboundRequest.getScript() != orderState.getScript()) {
//            throw new RuntimeException(String.format(
//                    "Invalid Inbound trade response bearing brokerId: %s, requestId: %s, orderResponse: %s",
//                    exchangeTradeRequest.getBrokerId(), orderOutboundRequest.getOrderId(), orderOutboundRequest));
//        }
//
//    }
}
