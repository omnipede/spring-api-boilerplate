package io.omnipede.boilerplate.system.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST(400, 40000, "Bad request"),
    UNAUTHORIZED(401, 40100, "Unauthorized"),
    NOT_FOUND(404, 40400, "Not found"),
    CONFLICT(409, 40900, "Conflict"),
    INTERNAL_SERVER_ERROR(500, 50000, "Internal server error.");

    private final int status;
    private final int code;
    private final String message;

    ErrorCode(final int status, final int code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
