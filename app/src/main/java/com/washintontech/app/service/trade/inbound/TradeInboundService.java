package com.washintontech.app.service.trade.inbound;

import com.washintontech.app.fix.FixApplication;
import com.washintontech.app.model.trade.ClientAccount;
import com.washintontech.app.model.trade.ScriptAccount;
import com.washintontech.app.model.trade.inbound.OrderCancelReq;
import com.washintontech.app.model.trade.inbound.OrderModifyReq;
import com.washintontech.app.model.trade.inbound.OrderNewReq;
import com.washintontech.app.model.trade.inbound.TradeCancelRequest;
import com.washintontech.app.model.trade.inbound.TradeModifyRequest;
import com.washintontech.app.model.trade.inbound.TradeNewRequest;
import com.washintontech.app.model.trade.inbound.TradeResponse;
import com.washintontech.app.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.OrdType;
import quickfix.field.OrigClOrdID;
import quickfix.field.Side;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.UUID;

@Service
public class TradeInboundService {

    @Value("${broker.id}")
    private String brokerId;

    private final FixApplication fixApplication;
    private final ClientRepository clientRepository;

    public TradeInboundService(final FixApplication fixApplication, final ClientRepository clientRepository) {
        this.fixApplication = fixApplication;
        this.clientRepository = clientRepository;
    }

    public TradeResponse trade(final TradeNewRequest tradeNewRequest) {
        fixApplication.sendToExchange(fixRequest(tradeNewRequest));
        return new TradeResponse("", "", Set.of());
    }

    public TradeResponse trade(final TradeModifyRequest tradeModifyRequest) throws FieldNotFound {
        fixApplication.sendToExchange(createModifyOrder(tradeModifyRequest));
        return new TradeResponse("", "", Set.of());
    }

    public TradeResponse trade(final TradeCancelRequest tradeCancelRequest) throws FieldNotFound {
        fixApplication.sendToExchange(createCancelOrder(tradeCancelRequest));
        return new TradeResponse("", "", Set.of());
    }

    private OrderCancelReplaceRequest createModifyOrder(final TradeModifyRequest tradeModifyRequest) throws FieldNotFound {
        final var requestedOrder = tradeModifyRequest.order();
        final var clientAccount = clientRepository.getClientAccount(tradeModifyRequest.clientId());
        final ClOrdID latestClOrdID;
        if (clientAccount != null) {
            final var scriptAccount = clientAccount.getScriptAccountMap().get(requestedOrder.script().getValue());
            if (scriptAccount != null) {
                final var cancelRequest = scriptAccount.getOrderCancelRequest();
                if (cancelRequest != null) {
                    throw new RuntimeException("Order cancel request already exists for script: " + requestedOrder.script().getValue());
                }
                final var orderCancelReplaceRequests = scriptAccount.getOrderCancelReplaceRequests();
                if (!orderCancelReplaceRequests.empty()) {
                    latestClOrdID = orderCancelReplaceRequests.peek().getClOrdID();
                } else if (scriptAccount.getNewOrderSingle() != null) {
                    latestClOrdID = scriptAccount.getNewOrderSingle().getClOrdID();
                } else {
                    throw new FieldNotFound("No order found for script: " + requestedOrder.script().getValue());
                }

                final var replaceRequest = getReplaceRequest(requestedOrder, latestClOrdID);
                replaceRequest.set(new Account(brokerId));
                scriptAccount.getOrderCancelReplaceRequests().push(replaceRequest);
                return replaceRequest;
            }
        }
        throw new RuntimeException("No client account found for clientId: " + tradeModifyRequest.clientId());
    }

    private OrderCancelReplaceRequest getReplaceRequest(final OrderModifyReq requestedOrder, final ClOrdID latestClOrdID) {
        final var ordTypeChar = requestedOrder.orderTypeChar() == '1' ? OrdType.MARKET : OrdType.LIMIT;
        return new OrderCancelReplaceRequest(
                new OrigClOrdID(latestClOrdID.getValue()),
                getClOrdID(),
                new Side(requestedOrder.sideChar()),
                getTransactTime(),
                new OrdType(ordTypeChar));
    }


    private NewOrderSingle fixRequest(final TradeNewRequest tradeNewRequest) {
        final var orderTypeChar = tradeNewRequest.order().orderTypeChar();
        if (orderTypeChar == '1') {
            return createNewMarketOrder(tradeNewRequest);
        } else if (orderTypeChar == '2') {
            return createNewLimitOrder(tradeNewRequest);
        } else {
            throw new IllegalArgumentException("Unsupported order type: " + orderTypeChar);
        }
    }

    private NewOrderSingle createNewMarketOrder(final TradeNewRequest tradeNewRequest) {
        final var requestedOrder = tradeNewRequest.order();
        final var order = createNewOrderSingle(requestedOrder, OrdType.MARKET);

        return fillNewOrderSingle(tradeNewRequest, order, requestedOrder);
    }

    private NewOrderSingle createNewLimitOrder(final TradeNewRequest tradeNewRequest) {
        final var requestedOrder = tradeNewRequest.order();
        final var order = createNewOrderSingle(requestedOrder, OrdType.LIMIT);

        order.set(requestedOrder.price());

        return fillNewOrderSingle(tradeNewRequest, order, requestedOrder);
    }


    private NewOrderSingle createNewOrderSingle(final OrderNewReq requestedOrder, final char limit) {
        return new NewOrderSingle(
                getClOrdID(),
                new Side(requestedOrder.sideChar()),
                getTransactTime(),
                new OrdType(limit));
    }

    private TransactTime getTransactTime() {
        return new TransactTime(LocalDateTime.now(ZoneId.of("UTC")));
    }

    private ClOrdID getClOrdID() {
        return new ClOrdID("ORDER-" + UUID.randomUUID());
    }

    private Message createCancelOrder(final TradeCancelRequest tradeCancelRequest) throws FieldNotFound {
        final var requestedOrder = tradeCancelRequest.order();
        final var clientAccount = clientRepository.getClientAccount(tradeCancelRequest.clientId());
        final ClOrdID latestClOrdID;
        if (clientAccount != null) {
            final var scriptAccount = clientAccount.getScriptAccountMap().get(requestedOrder.script().getValue());
            if (scriptAccount != null) {
                final var cancelRequest = scriptAccount.getOrderCancelRequest();
                if (cancelRequest != null) {
                    throw new RuntimeException("Order cancel request already exists for script: " + requestedOrder.script().getValue());
                }
                final var orderCancelReplaceRequests = scriptAccount.getOrderCancelReplaceRequests();
                if (!orderCancelReplaceRequests.empty()) {
                    latestClOrdID = orderCancelReplaceRequests.peek().getClOrdID();
                } else if (scriptAccount.getNewOrderSingle() != null) {
                    latestClOrdID = scriptAccount.getNewOrderSingle().getClOrdID();
                } else {
                    throw new FieldNotFound("No order found for script: " + requestedOrder.script().getValue());
                }

                final var orderCancelRequest = getOrderCancelRequest(tradeCancelRequest, latestClOrdID, requestedOrder);
                scriptAccount.setOrderCancelRequest(orderCancelRequest);
                return orderCancelRequest;
            }
        }

        throw new RuntimeException("No client account found for clientId: " + tradeCancelRequest.clientId());
    }

    private OrderCancelRequest getOrderCancelRequest(final TradeCancelRequest tradeCancelRequest,
                                                     final ClOrdID latestClOrdID, final OrderCancelReq requestedOrder) {
        final var orderCancelRequest = new OrderCancelRequest(
                new OrigClOrdID(latestClOrdID.getValue()),
                getClOrdID(),
                new Side(requestedOrder.sideChar()),
                getTransactTime());

        orderCancelRequest.set(new Account(tradeCancelRequest.clientId()));
        orderCancelRequest.set(requestedOrder.script());
        orderCancelRequest.set(requestedOrder.quantity());
        orderCancelRequest.set(new Account(brokerId));
        return orderCancelRequest;
    }

    private NewOrderSingle fillNewOrderSingle(final TradeNewRequest tradeNewRequest, final NewOrderSingle order,
                                              final OrderNewReq requestedOrder) {
        order.set(new Account(tradeNewRequest.clientId()));
        order.set(requestedOrder.script());
        order.set(requestedOrder.quantity());
        order.set(new Account(brokerId));

        final var timeInForce = requestedOrder.timeInForceChar() != null
                ? new TimeInForce(requestedOrder.timeInForceChar())
                : TimeInForce.DAY;

        assert timeInForce instanceof TimeInForce;
        order.set((TimeInForce) timeInForce);

        final var clientAccount = new ClientAccount(tradeNewRequest.clientId());
        clientAccount.getScriptAccountMap()
                .put(requestedOrder.script().getValue(),
                        new ScriptAccount(requestedOrder.script().getValue(), order));

        clientRepository.addClientAccount(clientAccount);
        return order;
    }

}
