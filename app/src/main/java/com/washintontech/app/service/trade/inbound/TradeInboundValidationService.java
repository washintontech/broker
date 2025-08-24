package com.washintontech.app.service.trade.inbound;

import com.washintontech.app.exception.BadRequestException;
import com.washintontech.app.model.trade.inbound.TradeCancelRequest;
import com.washintontech.app.model.trade.inbound.TradeModifyRequest;
import com.washintontech.app.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import quickfix.field.Symbol;

@Service
@RequiredArgsConstructor
public class TradeInboundValidationService {

    private final ClientRepository clientRepository;

    public void validate(final TradeCancelRequest tradeCancelRequest) {
        validateRequest(tradeCancelRequest.clientId(), tradeCancelRequest.order().script());
    }

    public void validate(final TradeModifyRequest tradeModifyRequest) {
        validateRequest(tradeModifyRequest.clientId(), tradeModifyRequest.order().script());
    }

    private void validateRequest(final String clientId, final Symbol symbol) {
        var clientAccount = clientRepository.getClientAccount(clientId);
        if (clientAccount == null) {
            throw new BadRequestException("Client account not found for clientId: " + clientId);
        }

        final var scriptValue = symbol.getValue();
        final var activeScriptOrder = clientAccount.getActiveScriptOrder(scriptValue);
        if (activeScriptOrder == null) {
            throw new BadRequestException("No active script order found for script: " + scriptValue);
        }
    }
}
