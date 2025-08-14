package com.washintontech.app.service;

import com.washintontech.app.model.updater.UpdateRequestExt;
import com.washintontech.updater.ScriptRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UpdaterService {
    //private final UpdaterServiceGrpc.UpdaterServiceStub updaterServiceStub;
    private final UserService userService;

    public UpdaterService(final UserService userService) {
        //this.updaterServiceStub = UpdaterServiceGrpc.newStub(managedChannel);
        this.userService = userService;
    }

//    public Flux<UpdateResponseExt> update(final String clientId, final UpdateRequestExt updateRequest) {
//        return Mono.just(updateRequest)
//                .flatMap(req -> getUpdatedScripts(clientId, req))
//                .flatMap(scripts -> getScriptRequests(scripts, updateRequest.verboseLevel()))
//                .flatMap(scriptRequests -> Mono.just(SubscribedScriptsRequest.newBuilder()
//                        .addAllUpdateRequest(scriptRequests)
//                        .build()))
//                .flatMapMany(subscribedReq -> Flux.create((FluxSink<UpdateResponseExt> sink) -> {
//                    final var requestStreamObserver = updaterServiceStub.rate(new UpdaterStreamObserverResponse(sink));
//                    requestStreamObserver.onNext(subscribedReq);
//                }));
//    }

    private Mono<Set<Integer>> getUpdatedScripts(final String clientId, final UpdateRequestExt request) {
        final var mutableSubscribedScripts = new HashSet<>(userService.subscribedScripts(clientId));
        mutableSubscribedScripts.removeAll(request.removalScripts());
        mutableSubscribedScripts.addAll(request.additionalScripts());
        return userService.updateSubscribedScripts(clientId, mutableSubscribedScripts);
    }

//    public Flux<UpdateResponseExt> update(final String clientId, final String verboseLevel,
//                                          final Set<Integer> additionalScripts,
//                                          final Set<Integer> removalScripts) {
//
//        final var subscribedScripts = getUpdatedScripts(clientId, additionalScripts, removalScripts);
//        final var scriptRequests = getScriptRequests(subscribedScripts, verboseLevel);
//        final var subscribedScriptsRequest = SubscribedScriptsRequest.newBuilder()
//                .addAllUpdateRequest(scriptRequests)
//                .build();
//
//        return Flux.create(sink -> {
//            final var requestStreamObserver = updaterServiceStub.rate(new UpdaterStreamObserverResponse(sink));
//            requestStreamObserver.onNext(subscribedScriptsRequest);
//        });
//    }

    private Mono<Set<ScriptRequest>> getScriptRequests(final Set<Integer> subscribedScripts, final Integer verboseLevel) {
        final var verboseLevelValue = verboseLevel == null ? 0 : verboseLevel;
        return Mono.just(subscribedScripts.stream()
                .map(script -> ScriptRequest.newBuilder()
                        .setScriptValue(script)
                        .setVerboseLevelValue(verboseLevelValue)
                        .build())
                .collect(Collectors.toSet()));
    }

//    private int getVerboseLevelValue(final String verboseLevel) {
//        return verboseLevel == null
//                ? 0
//                : Integer.parseInt(verboseLevel);
//    }

//    private Set<Integer> getUpdatedScripts(final String clientId,
//                                           final Set<Integer> additionalScripts,
//                                           final Set<Integer> removalScripts) {
//        final var mutableSubscribedScripts = new HashSet<>(userService.subscribedScripts(clientId));
//        mutableSubscribedScripts.removeAll(removalScripts);
//        mutableSubscribedScripts.addAll(additionalScripts);
//        return userService.updateSubscribedScripts(clientId, mutableSubscribedScripts);
//    }
}
