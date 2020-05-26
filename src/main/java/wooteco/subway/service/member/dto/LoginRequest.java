package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;

public class LoginRequest {
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;
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
