package com.washintontech.app.service.trade.inbound;

import com.washintontech.app.fix.FixApplication;
import com.washintontech.app.model.trade.ClientAccount;
import com.washintontech.app.model.trade.ScriptOrder;
import com.washintontech.app.model.trade.inbound.OrderCancelReq;
import com.washintontech.app.model.trade.inbound.OrderModifyReq;
import com.washintontech.app.model.trade.inbound.OrderNewReq;
import com.washintontech.app.model.trade.inbound.TradeCancelRequest;
import com.washintontech.app.model.trade.inbound.TradeModifyRequest;
import com.washintontech.app.model.trade.inbound.TradeNewRequest;
import com.washintontech.app.model.trade.inbound.TradeResponse;
import com.washintontech.app.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quickfix.FieldNotFound;
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
import java.util.Stack;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TradeInboundService {

    @Value("${broker.id}")
    private String brokerId;

    private final FixApplication fixApplication;
    private final ClientRepository clientRepository;
    private final TradeInboundValidationService tradeInboundValidationService;

    public TradeResponse trade(final TradeNewRequest tradeNewRequest) throws FieldNotFound {
        final var newOrderSingle = fixRequest(tradeNewRequest);
        saveRequest(tradeNewRequest, newOrderSingle);
        fixApplication.sendToExchange(newOrderSingle);
        return new TradeResponse(newOrderSingle.getClOrdID().getValue());
    }

    public TradeResponse trade(final TradeModifyRequest tradeModifyRequest) throws FieldNotFound {
        tradeInboundValidationService.validate(tradeModifyRequest);
        final var modifyOrder = createModifyOrder(tradeModifyRequest);
        saveRequest(tradeModifyRequest.clientId(), tradeModifyRequest.order().script().getValue(), modifyOrder);
        fixApplication.sendToExchange(modifyOrder);
        return new TradeResponse(modifyOrder.getClOrdID().getValue());
    }

    public TradeResponse trade(final TradeCancelRequest tradeCancelRequest) throws FieldNotFound {
        tradeInboundValidationService.validate(tradeCancelRequest);
        final var cancelOrder = createCancelOrder(tradeCancelRequest);
        saveRequest(tradeCancelRequest.clientId(), tradeCancelRequest.order().script().getValue(), cancelOrder);
        fixApplication.sendToExchange(cancelOrder);
        return new TradeResponse(cancelOrder.getClOrdID().getValue());
    }

    private OrderCancelRequest createCancelOrder(final TradeCancelRequest tradeCancelRequest) throws FieldNotFound {
        final var requestedOrder = tradeCancelRequest.order();
        final var scriptAccount = clientRepository.getClientAccount(tradeCancelRequest.clientId())
                .getActiveScriptOrder(requestedOrder.script().getValue());
        final var latestClOrdID = scriptAccount.getLatestClientOrderId();
        final var orderCancelRequest = getOrderCancelRequest(tradeCancelRequest, latestClOrdID, requestedOrder);
        scriptAccount.setOrderCancelRequest(orderCancelRequest);
        return orderCancelRequest;
    }

    private OrderCancelReplaceRequest createModifyOrder(final TradeModifyRequest tradeModifyRequest) {
        final var requestedOrder = tradeModifyRequest.order();
        final var scriptAccount = clientRepository.getClientAccount(tradeModifyRequest.clientId())
                .getActiveScriptOrder(requestedOrder.script().getValue());
        final var latestClOrdID = scriptAccount.getLatestClientOrderId();
        final var replaceRequest = getReplaceRequest(requestedOrder, latestClOrdID);
        replaceRequest.set(new Account(brokerId));
        replaceRequest.set(new Account(tradeModifyRequest.clientId()));
        scriptAccount.getOrderCancelReplaceRequests().push(replaceRequest);
        return replaceRequest;
    }

    private OrderCancelReplaceRequest getReplaceRequest(final OrderModifyReq requestedOrder, final String latestClOrdID) {
        final var ordTypeChar = requestedOrder.orderTypeChar() == '1' ? OrdType.MARKET : OrdType.LIMIT;
        return new OrderCancelReplaceRequest(
                new OrigClOrdID(latestClOrdID),
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
        order.set(new Account(tradeNewRequest.clientId()));
        return fillNewOrderSingle(tradeNewRequest, order, requestedOrder);
    }

    private NewOrderSingle createNewLimitOrder(final TradeNewRequest tradeNewRequest) {
        final var requestedOrder = tradeNewRequest.order();
        final var order = createNewOrderSingle(requestedOrder, OrdType.LIMIT);
        order.set(new Account(tradeNewRequest.clientId()));
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


    private OrderCancelRequest getOrderCancelRequest(final TradeCancelRequest tradeCancelRequest,
                                                     final String latestClOrdID,
                                                     final OrderCancelReq requestedOrder) {
        final var orderCancelRequest = new OrderCancelRequest(
                new OrigClOrdID(latestClOrdID),
                getClOrdID(),
                new Side(requestedOrder.sideChar()),
                getTransactTime());
        orderCancelRequest.set(new Account(tradeCancelRequest.clientId()));
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
        return order;
    }

    private void saveRequest(final TradeNewRequest tradeNewRequest, final NewOrderSingle newOrderSingle) throws FieldNotFound {
        var clientAccount = clientRepository.getClientAccount(tradeNewRequest.clientId());
        if (clientAccount == null) {
            clientAccount = new ClientAccount(tradeNewRequest.clientId());
        }
        final var scriptValue = tradeNewRequest.order().script().getValue();
        final var scriptOrdersStack = clientAccount.scriptAccountMap().getOrDefault(scriptValue, new Stack<ScriptOrder>());
        scriptOrdersStack.push(new ScriptOrder(scriptValue, newOrderSingle));
        clientAccount.scriptAccountMap()
                .put(scriptValue, scriptOrdersStack);

        clientRepository.addClientAccount(clientAccount);
    }

    private void saveRequest(final String clientId, final String script, final OrderCancelRequest cancelOrder) throws FieldNotFound {
        final var scriptOrder = clientRepository.getClientAccount(clientId).getActiveScriptOrder(script);
        scriptOrder.setOrderCancelRequest(cancelOrder);
        scriptOrder.setOrderStatus(ScriptOrder.OrderStatus.CANCEL);
        scriptOrder.setLatestClientOrderId(cancelOrder.getClOrdID().getValue());
    }

    private void saveRequest(final String clientId, final String script, final OrderCancelReplaceRequest modifyOrder) throws FieldNotFound {
        final var scriptOrder = clientRepository.getClientAccount(clientId).getActiveScriptOrder(script);
        scriptOrder.getOrderCancelReplaceRequests().push(modifyOrder);
        scriptOrder.setOrderStatus(ScriptOrder.OrderStatus.REPLACED);
        scriptOrder.setLatestClientOrderId(modifyOrder.getClOrdID().getValue());
    }
}
