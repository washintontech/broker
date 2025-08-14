package com.washintontech.app;

import com.washintontech.app.config.GrpcServer;
import io.grpc.ManagedChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import quickfix.SocketInitiator;

import java.util.concurrent.TimeUnit;

public class ShutDownListener implements ApplicationListener<ContextClosedEvent> {

    private static final Logger log = LogManager.getLogger(ShutDownListener.class);

    @Override
    public void onApplicationEvent(final ContextClosedEvent event) {
        event.getApplicationContext().getBean(GrpcServer.class)
                .stop();

        try {
            event.getApplicationContext().getBean(ManagedChannel.class)
                    .shutdownNow()
                    .awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Error occurred while waiting at Managed channel termination", e);
        }

        event.getApplicationContext().getBean(SocketInitiator.class)
                .stop();


        log.info("""

                    Matching Engine Client is shutting down. Bye!        \s
                """);
    }
}
