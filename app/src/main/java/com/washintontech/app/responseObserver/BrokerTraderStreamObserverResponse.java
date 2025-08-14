//package com.washintontech.app.responseObserver;
//
//import com.washintontech.app.model.trade.inbound.OrderRes;
//import com.washintontech.app.model.trade.inbound.TradeResponse;
//import com.washintontech.app.repository.TradeRepository;
//import com.washintontech.app.service.trade.inbound.TradeInboundValidationService;
//import com.washintontech.broker.trade.BrokerTradeResponse;
//import io.grpc.stub.StreamObserver;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import reactor.core.publisher.MonoSink;
//
//public class BrokerTraderStreamObserverResponse implements StreamObserver<BrokerTradeResponse> {
//
//    private static final Logger log = LogManager.getLogger(BrokerTraderStreamObserverResponse.class);
//    private final MonoSink<TradeResponse> tradeResponseMonoSink;
//    private final TradeRepository tradeRepository;
//    private final TradeInboundValidationService tradeInboundValidationService;
//
//    public BrokerTraderStreamObserverResponse(final MonoSink<TradeResponse> tradeResponseMonoSink,
//                                              final TradeRepository tradeRepository,
//                                              final TradeInboundValidationService tradeInboundValidationService) {
//        this.tradeResponseMonoSink = tradeResponseMonoSink;
//        this.tradeRepository = tradeRepository;
//        this.tradeInboundValidationService = tradeInboundValidationService;
//    }
//
//    @Override
//    public void onNext(final BrokerTradeResponse tradeResponse) {
//        tradeInboundValidationService.responseValidate(tradeResponse);
//        tradeRepository.processTradeInboundResponse(tradeResponse);
//        this.tradeResponseMonoSink.success(OrderRes.toOrderResponse(tradeResponse));
//
////        switch (orderStatus) {
////            case ACCEPTED -> {
////                tradeRepository.processTradeRecord(tradeResponse);
////                this.tradeResponseMonoSink.success(OrderRes.toOrderResponse(tradeResponse));
////            }
////            case REJECTED -> {
////                tradeRepository.processTradeRecord(tradeResponse);
////                this.tradeResponseMonoSink.success(OrderRes.toOrderResponse(tradeResponse));
////            }
////            default -> {
////                log.error("Unknown Response, having status: {}", orderStatus);
////            }
////        }
//    }
//
//    @Override
//    public void onError(final Throwable throwable) {
//        this.tradeResponseMonoSink.error(throwable);
//    }
//
//    @Override
//    public void onCompleted() {
//
//    }
//}
