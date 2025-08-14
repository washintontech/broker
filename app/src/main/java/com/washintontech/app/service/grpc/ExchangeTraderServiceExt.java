//package com.washintontech.app.service.grpc;
//
//import com.google.protobuf.Empty;
//import com.washintontech.app.annotation.ServerService;
//import com.washintontech.app.service.trade.outbound.TradeOutboundService;
//import com.washintontech.exchange.trade.ExchangeTradeRequest;
//import com.washintontech.exchange.trade.ExchangeTradeServiceGrpc;
//import io.grpc.stub.StreamObserver;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import static com.washintontech.app.util.LoggerUtil.logOnNext;
//
//@ServerService
//@Service
//public class ExchangeTraderServiceExt extends ExchangeTradeServiceGrpc.ExchangeTradeServiceImplBase {
//    private static final Logger log = LogManager.getLogger(ExchangeTraderServiceExt.class);
//    private final TradeOutboundService tradeOutboundService;
//
//    public ExchangeTraderServiceExt(final TradeOutboundService tradeOutboundService) {
//        this.tradeOutboundService = tradeOutboundService;
//    }
//
//    @Override
//    public void trade(ExchangeTradeRequest request, StreamObserver<Empty> responseObserver) {
//        Mono.just(request)
//                .doOnEach(logOnNext(req -> log.debug("Request received: {}", req)))
//                .flatMap(tradeOutboundService::processExecutedTrade)
//                .subscribe();
//    }
//}
