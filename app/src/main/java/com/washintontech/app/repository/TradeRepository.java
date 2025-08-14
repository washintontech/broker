package com.washintontech.app.repository;

import com.washintontech.app.domain.OrderState;
import com.washintontech.exchange.trade.ExchangeTradeRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class TradeRepository {
    /**
     * Broker maintains each client’s order history, fills, cancellations, and positions.
     */
    private static final Logger log = LogManager.getLogger(TradeRepository.class);
    private final Map<String, OrderState> orderByRequestId;
    private final Map<String, OrderState> orderByOrderId;
    private final Map<String, Set<OrderState>> ordersByClientId;
    private final Map<String, OrderState> tradedOrderByOrderId;

    public TradeRepository() {
        this.orderByRequestId = new ConcurrentHashMap<>();
        this.orderByOrderId = new ConcurrentHashMap<>();
        this.ordersByClientId = new ConcurrentHashMap<>();
        this.tradedOrderByOrderId = new ConcurrentHashMap<>();
    }

    public OrderState orderStateByOrderId(final String orderId) {
        return orderByOrderId.get(orderId);
    }

    public OrderState orderStateByRequestId(final String requestId) {
        return orderByRequestId.get(requestId);
    }

//    public Mono<BrokerTradeRequest> processTradeInboundRequest(final EnrichedTradeInboundRequest enrichedTradeRequest) {
//        final var tradeRequest = enrichedTradeRequest.getTradeRequest();
//        final var brokerTradeRequest = enrichedTradeRequest.getBrokerTradeRequest();
//        final var order = brokerTradeRequest.getOrderInboundRequest();
//        final var orderState = new OrderState(brokerTradeRequest.getRequestId(), tradeRequest.clientId(),
//                order.getOrderId(), order.getScript(), order.getTradeDirection(), order.getQuantity(), order.getPrice());
//        orderByRequestId.put(brokerTradeRequest.getRequestId(), orderState);
//        if (order.getOrderType() == OrderType.NEW_MARKET || order.getOrderType() == OrderType.NEW_LIMIT) {
//            orderByOrderId.put(order.getOrderId(), orderState);
//            ordersByClientId.compute(tradeRequest.clientId(), (clientId, orderStates) -> {
//                orderStates = orderStates == null
//                        ? new HashSet<>()
//                        : orderStates;
//                orderStates.add(orderState);
//                return orderStates;
//            });
//        }
//        return Mono.just(brokerTradeRequest);
//    }

//    public void processTradeInboundResponse(final BrokerTradeResponse tradeResponse) {
//        final var orderState = orderByRequestId.get(tradeResponse.getRequestId());
//        orderState.setTransactionState(TransactionState.ORDER_PLACED);
//    }

    /**
     * string orderId = 1;
     * common.Script script = 2;
     * uint32 quantity = 3;
     * optional ExecutionStatus executionStatus = 4;
     * optional common.OrderType orderType = 5;
     * optional float orderExecutionPrice = 6;
     */
    public Mono<ExchangeTradeRequest> storeExecutedTrade(final ExchangeTradeRequest exchangeTradeRequest) {
        final var orderOutboundRequest = exchangeTradeRequest.getOrderOutboundRequest();
        orderByOrderId.get(orderOutboundRequest.getOrderId())
                .addTransaction(orderOutboundRequest);
        return Mono.just(exchangeTradeRequest);
    }


//    public boolean validateInboundResponse(final BrokerTradeResponse tradeResponse) {
//        final var orderState = orderByRequestId.get(tradeResponse.getRequestId());
//        final var orderInboundResponse = tradeResponse.getOrderInboundResponse();
//        return orderState != null &&
//                orderInboundResponse.getQuantity() == orderState.getTotalQuantity() &&
//                orderInboundResponse.getTradeDirection() == orderState.getTradeDirection() &&
//                orderInboundResponse.getScript() == orderState.getScript() &&
//                orderInboundResponse.getOrderPlacedPrice() == orderState.getPrice();
//    }


// Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

//    private final ConcurrentHashMap<String, Sinks.Many<Runnable>> clientQueues = new ConcurrentHashMap<>();
//
//    public Mono<Void> process(String clientId) {
//        return Mono.create(sink -> {
//            var queue = clientQueues.computeIfAbsent(clientId, id -> {
//                Sinks.Many<Runnable> newQueue = Sinks.many().unicast().onBackpressureBuffer();
//                processQueue(clientId, newQueue);
//                return newQueue;
//            });
//
//            queue.emitNext(() -> {
//                // ✅ Do actual processing
//                System.out.println("Processing: " + clientId + " on thread " + Thread.currentThread().getName());
//                sink.success(); // Complete the outer Mono when processing finishes
//            }, Sinks.EmitFailureHandler.FAIL_FAST);
//        });
//    }
//
//    private void processQueue(String clientId, Sinks.Many<Runnable> queue) {
//        queue.asFlux()
//                .concatMap(runnable -> Mono.fromRunnable(runnable).subscribeOn(reactor.core.scheduler.Schedulers.parallel()))
//                .doFinally(signal -> clientQueues.remove(clientId)) // optional: cleanup
//                .subscribe();
//    }


}
