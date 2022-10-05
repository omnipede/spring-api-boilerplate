package io.omnipede.boilerplate.system.exception;

import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

class SystemExceptionTest {

    @Test
    @DisplayName("객체가 생성되어야 한다")
    public void tc1() {

        // Given
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        // When
        Throwable throwable = catchThrowable(() -> {
            throw new SystemException(errorCode);
        });

        // Then
        assertThat(throwable).isInstanceOf(SystemException.class);
        ErrorCode given = ((SystemException)throwable).getErrorCode();
        AssertionsForInterfaceTypes.assertThat(given).isEqualTo(errorCode);
    }

    @Test
    @DisplayName("객체가 생성되어야 한다 (2)")
    public void tc2() {

        // Given
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        // When
        Throwable throwable = catchThrowable(() -> {
            try {
                throw new Exception("Hello world");
            } catch (Exception e) {
                throw new SystemException(errorCode, e);
            }
        });

        // Then
        assertThat(throwable).isInstanceOf(SystemException.class);
        assertThat(throwable).hasCauseInstanceOf(Exception.class);
        ErrorCode given = ((SystemException)throwable).getErrorCode();
        AssertionsForInterfaceTypes.assertThat(given).isEqualTo(errorCode);
    }

    @Test
    @DisplayName("객체가 생성되어야 한다 (3)")
    public void tc3() {

        // Given
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        String message = "Hello world";

        // When
        Throwable throwable = catchThrowable(() -> {
            throw new SystemException(errorCode, message);
        });

        // Then
        assertThat(throwable).isInstanceOf(SystemException.class);
        assertThat(throwable).hasMessage(message);
        ErrorCode given = ((SystemException)throwable).getErrorCode();
        AssertionsForInterfaceTypes.assertThat(given).isEqualTo(errorCode);
    }
}