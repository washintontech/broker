package com.washintontech.app.fix;

import com.washintontech.app.model.trade.inbound.OrderNewReq;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import quickfix.UnsupportedMessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OutboundFixApplication extends MessageCracker implements Application {

    private static final Logger log = LogManager.getLogger(OutboundFixApplication.class);
    private final Map<SessionID, Session> activeSessions = new ConcurrentHashMap<>();

    @Override
    public void onCreate(final SessionID sessionId) {
        activeSessions.put(sessionId, Session.lookupSession(sessionId));
        log.info("Session created: {}", sessionId);
    }

    @Override
    public void onLogout(final SessionID sessionId) {
        activeSessions.remove(sessionId);
        log.info("Session terminated: {}", sessionId);
    }

    @Override
    public void fromApp(final Message message, final SessionID sessionId) throws FieldNotFound, IncorrectDataFormat,
            IncorrectTagValue, UnsupportedMessageType {
        crack(message, sessionId);
    }

    @Handler
    public void onMessage(OrderNewReq order, SessionID sessionID) throws FieldNotFound {

    }

    @Override
    public void onLogon(final SessionID sessionId) {
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

}
