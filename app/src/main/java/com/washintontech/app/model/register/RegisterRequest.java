package com.washintontech.app.model.register;

import com.washintontech.user.UserRequest;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank String name, @NotBlank String govtId) {
    public UserRequest toUserRequest() {
        return UserRequest.newBuilder()
                .setName(this.name)
                .setGovtId(this.govtId)
                .build();
    }
}
