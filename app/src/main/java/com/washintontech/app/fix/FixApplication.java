package com.washintontech.app.fix;

import com.washintontech.app.service.trade.outbound.TradeOutboundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.ExecutionReport;

@Component
@RequiredArgsConstructor
@Log4j2
public class FixApplication extends MessageCracker implements Application {

    private final TradeOutboundService tradeOutboundService;
    private SessionID sessionID;

    @Override
    public void onCreate(final SessionID sessionId) {
        this.sessionID = sessionId;
        log.debug("Session created: {}", sessionId);
    }

    public void sendToExchange(Message message) {
        if (sessionID != null && Session.doesSessionExist(sessionID)) {
            try {
                log.info("Sending message to exchange: {}", message);
                Session.sendToTarget(message, sessionID);
            } catch (SessionNotFound e) {
                throw new RuntimeException(e);
            }
        } else {
            log.warn("No active session ");
        }
    }

    @Override
    public void onLogon(final SessionID sessionId) {

    }

    @Override
    public void onLogout(final SessionID sessionId) {

    }

    @Override
    public void toAdmin(final Message message, final SessionID sessionId) {

    }

    @Override
    public void fromAdmin(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

    }

    @Override
    public void toApp(final Message message, final SessionID sessionId) throws DoNotSend {

    }

    @Override
    public void fromApp(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        log.info("Received message: {}", message);
        crack(message, sessionId);
    }

    @Handler
    public void onMessage(ExecutionReport message, SessionID sessionID) throws FieldNotFound {
        log.info("Received ExecutionReport: {}", message);
        try {
            tradeOutboundService.processExecutionReport(message);
        } catch (Exception exception) {
            log.error("Error processing ExecutionReport: {}", exception.getMessage(), exception);
        }

    }
}
