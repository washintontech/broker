package com.washintontech.app.service;

import com.washintontech.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class UserService {
    //private final UserServiceGrpc.UserServiceStub userServiceStub;
    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        //this.userServiceStub = UserServiceGrpc.newStub(managedChannel);
        this.userRepository = userRepository;

    }

//    public Mono<RegisterResponse> register(final @Valid RegisterRequest request) {
//        return Mono.create(sink ->
//                userServiceStub.register(request.toUserRequest(), new UserStreamObserverResponse(sink)));
//    }

    public Set<Integer> subscribedScripts(final String clientId) {
        return userRepository.subscribedScripts(clientId);
    }

    public Mono<Set<Integer>> updateSubscribedScripts(final String clientId, final Set<Integer> subscribedScripts) {
        return Mono.just(userRepository.updateSubscribedScripts(clientId, subscribedScripts));
    }
}
