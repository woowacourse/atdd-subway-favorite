package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotEmpty;

public class LoginRequest {
    @NotEmpty
    private String email;
    @NotEmpty
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
