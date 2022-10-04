package io.omnipede.boilerplate.system.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
class RestError {

    private final int id;

    private final String message;

    private final String detail;    // 디테일 메시지

    public RestError(int id, String message, String detail) {

        this.id = id;
        this.message = message;
        this.detail = detail;
    }
}
