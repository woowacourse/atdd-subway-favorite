package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "이메일은 공란이 될 수 없습니다!")
    private String email;

    @NotBlank(message = "비밀번호는 공란이 될 수 없습니다!")
    private String password;

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
