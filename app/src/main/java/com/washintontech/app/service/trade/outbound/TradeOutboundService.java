package com.washintontech.app.service.trade.outbound;

import com.washintontech.app.model.trade.TradeAccount;
import com.washintontech.app.repository.ClientRepository;
import com.washintontech.app.repository.TradeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import quickfix.FieldNotFound;
import quickfix.fix44.ExecutionReport;

@Service
public class TradeOutboundService {
    private static final Logger log = LogManager.getLogger(TradeOutboundService.class);
    private final TradeRepository tradeRepository;
    private final TradeOutboundValidationService tradeOutboundValidationService;
    private final ClientRepository clientRepository;


    public TradeOutboundService(final TradeRepository tradeRepository,
                                final TradeOutboundValidationService tradeOutboundValidationService,
                                final ClientRepository clientRepository) {
        this.tradeRepository = tradeRepository;
        this.tradeOutboundValidationService = tradeOutboundValidationService;
        this.clientRepository = clientRepository;
    }

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
                final var clientAccount = clientRepository.getClientAccount(executionReport.getClOrdID().getValue());
                final var scriptAccount = clientAccount.getScriptAccountMap().get(executionReport.getSymbol().getValue());
                scriptAccount.getExecutionReports().push(executionReport);

                new TradeAccount(); // TODO
            }
        }
    }

    private void addExecutionReport(final ExecutionReport executionReport) throws FieldNotFound {
        final var clientAccount = clientRepository.getClientAccount(executionReport.getClOrdID().getValue());
        final var scriptAccount = clientAccount.getScriptAccountMap().get(executionReport.getSymbol().getValue());
        scriptAccount.getExecutionReports().push(executionReport);
    }
}
