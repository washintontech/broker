//package com.washintontech.app.config;
//
//import com.washintontech.app.fix.InboundFixApplication;
//import com.washintontech.app.fix.OutboundFixApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import quickfix.ConfigError;
//import quickfix.DefaultMessageFactory;
//import quickfix.FileStoreFactory;
//import quickfix.Initiator;
//import quickfix.ScreenLogFactory;
//import quickfix.SessionSettings;
//import quickfix.SocketInitiator;
//
//@Configuration
//public class FixConfig {
//
//    private InboundFixApplication inboundFixApplication;
//    private OutboundFixApplication outboundFixApplication;
//
//    @Bean // TODO: Check if bean exposure required
//    public Initiator fixInboundInitiator() throws ConfigError {
//        SessionSettings settings = new SessionSettings("inboundFix.cfg");
//        final var socketInitiator = new SocketInitiator(
//                inboundFixApplication, new FileStoreFactory(settings), settings,
//                new ScreenLogFactory(settings), new DefaultMessageFactory());
//        socketInitiator.start();
//        return socketInitiator;
//    }
//
//    @Bean // TODO: Check if bean exposure required
//    public Initiator fixOutboundInitiator() throws ConfigError {
//        SessionSettings settings = new SessionSettings("outboundFix.cfg");
//        final var socketInitiator = new SocketInitiator(
//                outboundFixApplication, new FileStoreFactory(settings), settings,
//                new ScreenLogFactory(settings), new DefaultMessageFactory());
//        socketInitiator.start();
//        return socketInitiator;
//    }
//
//}
