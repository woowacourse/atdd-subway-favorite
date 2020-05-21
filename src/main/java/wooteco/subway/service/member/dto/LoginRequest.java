package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class LoginRequest {
    @Email
    private String email;
    @Size(min = 4)
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
