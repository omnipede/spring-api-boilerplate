package io.omnipede.boilerplate.system.exception;

import javax.validation.constraints.NotEmpty;

class TestRequestDTO {

    @NotEmpty
    private String a;

    private Integer b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }
}
