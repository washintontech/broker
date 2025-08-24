package com.washintontech.app.service.trade.outbound;

import com.washintontech.app.model.trade.TradeOrder;
import com.washintontech.app.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import quickfix.FieldNotFound;
import quickfix.fix44.ExecutionReport;

@Service
@RequiredArgsConstructor
@Log4j2
public class TradeOutboundService {

    private final TradeOutboundValidationService tradeOutboundValidationService;
    private final ClientRepository clientRepository;

    public void processExecutionReport(final ExecutionReport executionReport) throws FieldNotFound {
        switch (executionReport.getExecType().getValue()) {
            case '0': // New Order
            {
                addExecutionReport(executionReport);
            }
            case '4': // Cancel Order
            {
                addExecutionReport(executionReport);
            }
            case '5': // Replace Order
            {
                addExecutionReport(executionReport);
            }
            case '2': // Trade
            {
                final var clientAccount = clientRepository.getClientAccount(executionReport.getAccount().getValue());
                final var scriptAccount = clientAccount.getActiveScriptOrder(executionReport.getSymbol().getValue());
                scriptAccount.getExecutionReports().push(executionReport);
                scriptAccount.getTradeOrders().add(createTradeOrder(executionReport));
            }
            case '8': // Rejected Order
            {
                addExecutionReport(executionReport);
            }
        }
    }

    private TradeOrder createTradeOrder(final ExecutionReport executionReport) throws FieldNotFound {
        return new TradeOrder(
                executionReport.getClOrdID().getValue(),
                executionReport.getOrderID().getValue(),
                executionReport.getSymbol().getValue(),
                executionReport.getSide().getValue(),
                executionReport.getOrderQty().getValue(),
                executionReport.getTransactTime().getValue());
    }

    private void addExecutionReport(final ExecutionReport executionReport) throws FieldNotFound {
        final var clientAccount = clientRepository.getClientAccount(executionReport.getAccount().getValue());
        final var scriptAccount = clientAccount.getScriptOrder(executionReport.getSymbol().getValue(), executionReport.getClOrdID().getValue());
        scriptAccount.getExecutionReports().push(executionReport);
    }
}
