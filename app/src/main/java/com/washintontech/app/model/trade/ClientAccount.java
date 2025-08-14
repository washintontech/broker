package com.washintontech.app.model.trade;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@RequiredArgsConstructor
public class ClientAccount {
    private String clientId;
    private Map<String, ScriptAccount> scriptAccountMap;

    public ClientAccount(final String clientId) {
        this.clientId = clientId;
        this.scriptAccountMap = new HashMap<>();
    }
}
