package com.washintontech.app;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import quickfix.SocketInitiator;

@Log4j2
public class ShutDownListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(final ContextClosedEvent event) {

        event.getApplicationContext().getBean(SocketInitiator.class)
                .stop();
        log.info("""
                
                    Stock Broker is shutting down. Bye!        \s
                """);
    }
}
