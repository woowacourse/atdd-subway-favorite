package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일형식을 지켜주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public LoginRequest() {
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
