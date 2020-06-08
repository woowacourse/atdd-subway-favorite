package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "EMAIL_EMPTY")
    @Email(message = "EMAIL_INVALID")
    private String email;
    @NotBlank(message = "PASSWORD_EMPTY")
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
