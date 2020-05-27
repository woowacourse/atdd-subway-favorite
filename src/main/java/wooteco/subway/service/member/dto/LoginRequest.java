package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginRequest {
    @Email(message = "메일의 양식을 지켜주세요!")
    private String email;

    @Size(min = 10, message = "패스워드는 최소 10자를 입력해주셔야 합니다!")
    @NotNull(message = "패스워드를 입력해주세요!")
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
