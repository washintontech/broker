//package com.washintontech.app.service;
//
//import com.washintontech.user.UserServiceGrpc;
//import io.grpc.ManagedChannel;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ServiceInitializer {
//
//    //@Bean("UserService")
//    @Bean
//    public UserServiceGrpc.UserServiceStub getUserService(final ManagedChannel managedChannel) {
//        return UserServiceGrpc.newStub(managedChannel);
//    }
//}
