package com.washintontech.app.model.common;

import com.washintontech.common.ResponseStatus;

public enum Status {
    UNKNOWN_STATUS,
    ACCEPTED,
    REJECTED;

    public static Status toStatus(ResponseStatus responseStatus) {
        switch (responseStatus) {
            case ACCEPTED -> {
                return Status.ACCEPTED;
            }
            case REJECTED -> {
                return Status.REJECTED;
            }

            default -> {
                return Status.UNKNOWN_STATUS;
            }
        }
    }
}
