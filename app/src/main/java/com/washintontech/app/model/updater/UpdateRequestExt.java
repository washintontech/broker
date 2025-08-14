package com.washintontech.app.model.updater;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Set;

public record UpdateRequestExt(@PositiveOrZero @Max(3) Integer verboseLevel,
                               Set<Integer> additionalScripts,
                               Set<Integer> removalScripts) {
}
