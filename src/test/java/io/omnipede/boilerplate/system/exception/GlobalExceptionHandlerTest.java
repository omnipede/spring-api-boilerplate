package io.omnipede.boilerplate.system.exception;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                TestConfig.class,
                TestController.class,
                GlobalExceptionHandler.class
        }
)
@WebAppConfiguration
class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                // Mock mvc 의 dispatcher servlet 에 대해서 handler 가 존재하지 않을 때
                // NoHandlerFoundException 을 발생시키도록 설정
                .addDispatcherServletCustomizer((customizer)
                        -> customizer.setThrowExceptionIfNoHandlerFound(true))
                .build();
    }

    @Test
    @DisplayName("MethodArgumentNotValidException 헨들링 테스트")
    public void tc1() throws Throwable{

        // Given
        // {
        //        "a": "",
        //        "b": 123
        // }
        String requestBody = "{\n" +
                "                \"a\": \"\",\n" +
                "                \"b\": 123\n" +
                "            }";

        // When
        mockMvc.perform(post("/api/v1/test")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("ConstraintViolationException 헨들링 테스트")
    public void tc11() throws Throwable {

        // When
        mockMvc.perform(get("/api/v1/test?id=1024")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("MethodArgumentTypeMismatchException 헨들링 테스트")
    public void testMethodArgumentTypeMismatchException() throws Exception {

        // When
        mockMvc.perform(get("/api/v1/test?id=abcdefghi")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("MissingServletRequestParameterException 헨들링 테스트")
    public void tc2() throws Throwable {

        // Given
        mockMvc.perform(get("/api/v1/test")
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("HttpMessageNotReadableException 헨들링 테스트")
    public void tc3() throws Throwable {

        // Given
        String requestBody = "{\n" +
                "                \"a\": \"\" ,,,,,  <-- Invalid JSON\n" +
                "            }";

        // When
        mockMvc.perform(
                        post("/api/v1/test")
                                .content(requestBody)
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("NoHandlerFoundException 헨들링 테스트")
    public void tc4() throws Throwable {

        // Given

        // When
        mockMvc.perform(
                        get("/api/v1/missing")
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("HttpRequestMethodNotSupportedException 헨들링 테스트")
    public void tc5() throws Exception {

        // Given

        // When
        mockMvc.perform(
                        put("/api/v1/test")
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("HttpMediaTypeNotSupportedException 헨들링 테스트")
    public void tc6() throws Exception {

        // When
        mockMvc.perform(
                        get("/api/v1/test?id=12345")
                                .contentType(MediaType.TEXT_EVENT_STREAM))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("SystemException 헨들링 테스트")
    public void tc7() throws Exception {

        // When
        mockMvc.perform(
                        get("/api/v1/test?id=2")
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").value("This is system exception"));
    }

    @Test
    @DisplayName("Exception 헨들링 테스트")
    public void tc8() throws Exception {

        // When
        mockMvc.perform(
                        get("/api/v1/test?id=1")
                                .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.INTERNAL_SERVER_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()))
                .andExpect(jsonPath("$.detail").value("This is exception"));
    }

    @Test
    @DisplayName("Log 레벨이 DEBUG 일 경우")
    public void tc9() throws Exception {

        // Given
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(GlobalExceptionHandler.class);
        logger.setLevel(Level.DEBUG);

        // When
        mockMvc.perform(get("/api/v1/test")
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }

    @Test
    @DisplayName("Log 레벨이 DEBUG 가 아닐 경우")
    public void tc10() throws Exception {

        // Given
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(GlobalExceptionHandler.class);
        logger.setLevel(Level.INFO);

        // When
        mockMvc.perform(get("/api/v1/test")
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ErrorCode.BAD_REQUEST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.BAD_REQUEST.getMessage()))
                .andExpect(jsonPath("$.detail").isString());
    }
}