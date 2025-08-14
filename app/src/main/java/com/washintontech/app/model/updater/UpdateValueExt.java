package com.washintontech.app.model.updater;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateValueExt(@NotBlank String script, @NotNull @PositiveOrZero float price) {
}
