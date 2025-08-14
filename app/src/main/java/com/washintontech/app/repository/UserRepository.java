package com.washintontech.app.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {

    private final Map<String, Set<Integer>> subscribedScriptsByClientId;

    public UserRepository() {
        this.subscribedScriptsByClientId = new ConcurrentHashMap<>();
        // Adding dummy values
        subscribedScriptsByClientId.put("123456", Set.of(2, 3, 1));

    }

    public Set<Integer> subscribedScripts(final String clientId) {
        return subscribedScriptsByClientId.get(clientId);
    }

    public Set<Integer> updateSubscribedScripts(final String clientId, final Set<Integer> subscribedScripts) {
        return subscribedScriptsByClientId.put(clientId, subscribedScripts);
    }
}
