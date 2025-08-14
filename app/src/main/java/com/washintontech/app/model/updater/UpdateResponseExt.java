package com.washintontech.app.model.updater;

import com.washintontech.updater.ResponsePayLoad;
import com.washintontech.updater.UpdateResponse;

import java.util.Map;
import java.util.stream.Collectors;

public record UpdateResponseExt(Map<Integer, UpdateValueExt> scriptUpdaterValueMap) {

    public static UpdateResponseExt toUpdateResponseExt(final UpdateResponse updateResponse) {
        final var updaterValueMap = updateResponse.getScriptPriceMapMap()
                .values()
                .stream()
                .collect(Collectors.toMap(
                        ResponsePayLoad::getScriptValue,
                        item -> new UpdateValueExt(item.getScript().name(),
                                (float) item.getCurrentPrice() / 100)));

        return new UpdateResponseExt(updaterValueMap);
    }
}
