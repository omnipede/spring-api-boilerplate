package io.omnipede.boilerplate.system.exception;


import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 테스트용 컨트롤러
 */
@RestController
@RequestMapping("/api/v1")
@Validated
class TestController {

    @GetMapping(value = "/test", headers = "content-type=application/json")
    public String get(@RequestParam @Range(min = 0, max = 10) Integer id) throws Exception {

        if (id == 1)
            throw new Exception("This is exception");
        if (id == 2)
            throw new SystemException(ErrorCode.BAD_REQUEST, "This is system exception");

        return "Hello world: " + id;
    }

    @PostMapping(value = "/test")
    public String post(@RequestBody @Valid TestRequestDTO dto) {

        if (dto.getA() != null)
            return dto.getA();
        return "Hello world";
    }
}
