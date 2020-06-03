package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @Email(message="이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank
    private String password;

    private LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
