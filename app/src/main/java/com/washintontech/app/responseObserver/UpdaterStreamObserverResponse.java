package com.washintontech.app.responseObserver;

import com.washintontech.app.model.updater.UpdateResponseExt;
import com.washintontech.updater.UpdateResponse;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.FluxSink;

public class UpdaterStreamObserverResponse implements StreamObserver<UpdateResponse> {

    private static final Logger log = LogManager.getLogger(UpdaterStreamObserverResponse.class);

    private final FluxSink<UpdateResponseExt> emitter;

    public UpdaterStreamObserverResponse(final FluxSink<UpdateResponseExt> emitter) {
        this.emitter = emitter;

    }

    @Override
    public void onNext(final UpdateResponse updateResponse) {
        log.info("UpdaterStreamObserverResponse: onNext updateResponse: {}", updateResponse);
        this.emitter.next(UpdateResponseExt.toUpdateResponseExt(updateResponse));
    }

    @Override
    public void onError(final Throwable throwable) {
        this.emitter.error(throwable);
    }

    @Override
    public void onCompleted() {
        this.emitter.complete();
    }
}
