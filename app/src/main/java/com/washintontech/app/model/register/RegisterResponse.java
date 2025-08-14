package com.washintontech.app.model.register;

import com.washintontech.app.model.common.Status;
import com.washintontech.user.UserResponse;

public record RegisterResponse(Status status, String name, String govtId, String clientId) {

    public static RegisterResponse toRegisterResponse(UserResponse userResponse) {
        return new RegisterResponse(Status.toStatus(userResponse.getStatus()),
                userResponse.getName(),
                userResponse.getGovtId(),
                userResponse.getClientId());
    }
}
