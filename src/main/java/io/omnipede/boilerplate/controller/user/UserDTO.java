package io.omnipede.boilerplate.controller.user;

import io.omnipede.boilerplate.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
class UserDTO {
    @NotNull
    @NotBlank
    @Email // 이메일 주소 형식 확인
    private String email;

    @NotNull
    @NotBlank
    private String password;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
