package com.washintontech.app.service;

import com.washintontech.app.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    private final ClientRepository clientRepository;

    public ReportService(final ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public void generateDayEndReportPerClient() { // TODO: Implement this method
        clientRepository.getClientAccountMap().values()
                .stream()
                .forEach(clientAccount -> {
                    clientAccount.scriptAccountMap().values()
                            .stream()
                            .forEach(scriptOrders -> {
                                int totalTradedQuantityBid;
                                int totalTradedQuantityAsk;
                                double priceBid;
                                double priceAsk;
                            });
                });
    }
}
