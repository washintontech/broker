package com.washintontech.app.model.trade.inbound;

import com.washintontech.broker.trade.BrokerTradeRequest;
import lombok.Builder;
import lombok.Value;
import quickfix.fix44.NewOrderSingle;

@Builder(toBuilder = true)
@Value
public class EnrichedTradeInboundRequest {
    private TradeNewRequest tradeNewRequest;
    private BrokerTradeRequest brokerTradeRequest;
    private NewOrderSingle newOrderSingle;

//    public EnrichedTradeInboundRequest(final TradeRequest tradeRequest) {
//        this.tradeRequest = tradeRequest;
//    }

//    public EnrichedTradeInboundRequest withTradeRequest(final TradeRequest tradeRequest) {
//        return this.toBuilder().tradeRequest(tradeRequest).build();
//    }

//    public EnrichedTradeInboundRequest withBrokerTradeRequest(final BrokerTradeRequest brokerTradeRequest) {
//        return this.toBuilder().brokerTradeRequest(brokerTradeRequest).build();
//    }
//
//    public EnrichedTradeInboundRequest withFixRequest(final NewOrderSingle newOrderSingle) {
//        return this.toBuilder().newOrderSingle(newOrderSingle).build();
//    }
}
