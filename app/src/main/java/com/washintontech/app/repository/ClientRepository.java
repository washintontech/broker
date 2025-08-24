package com.washintontech.app.repository;

import com.washintontech.app.model.trade.ClientAccount;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Getter
public class ClientRepository {

    private final Map<String, ClientAccount> clientAccountMap = new HashMap<>();

    public ClientAccount getClientAccount(String clientId) {
        return clientAccountMap.get(clientId);
    }

    public void addClientAccount(final ClientAccount clientAccount) {
        clientAccountMap.put(clientAccount.clientId(), clientAccount);
    }
}
