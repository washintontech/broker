package com.washintontech.app.responseObserver;

import com.washintontech.app.model.register.RegisterResponse;
import com.washintontech.user.UserResponse;
import io.grpc.stub.StreamObserver;
import reactor.core.publisher.MonoSink;

public class UserStreamObserverResponse implements StreamObserver<UserResponse> {

    private final MonoSink<RegisterResponse> userResponseMonoSink;

    public UserStreamObserverResponse(final MonoSink<RegisterResponse> sink) {
        this.userResponseMonoSink = sink;
    }

    @Override
    public void onNext(final UserResponse userResponse) {
        this.userResponseMonoSink.success(RegisterResponse.toRegisterResponse(userResponse));
    }

    @Override
    public void onError(final Throwable throwable) {
        this.userResponseMonoSink.error(throwable);
    }

    @Override
    public void onCompleted() {
    }
}
