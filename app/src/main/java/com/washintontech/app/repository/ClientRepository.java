package com.washintontech.app.repository;

import com.washintontech.app.model.trade.ClientAccount;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ClientRepository {

    private Map<String, ClientAccount> clientAccountMap = new HashMap<>();

    public ClientAccount getClientAccount(String clientId) {
        return clientAccountMap.get(clientId);
    }

    public void addClientAccount(ClientAccount clientAccount) {
        clientAccountMap.put(clientAccount.getClientId(), clientAccount);
    }
}
