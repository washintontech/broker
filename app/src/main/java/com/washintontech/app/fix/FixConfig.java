package com.washintontech.app.fix;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

import java.util.Objects;

@Configuration
public class FixConfig {
    private SocketInitiator initiator;

    @Bean
    public SocketInitiator socketInitiator(final FixApplication fixApplication) throws ConfigError {
        final var sessionSettings = new SessionSettings(Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream("quickfix/fix-session.cfg")));

        MessageStoreFactory storeFactory = new FileStoreFactory(sessionSettings);
        LogFactory logFactory = new FileLogFactory(sessionSettings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        this.initiator = new SocketInitiator(
                fixApplication, storeFactory, sessionSettings, logFactory, messageFactory);

        initiator.start();
        return initiator;
    }
}
