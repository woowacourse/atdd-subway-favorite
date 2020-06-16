package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Objects;

public class LoginRequest {
    @Email(message = "올바른 이메일 형식을 입력해주세요")
    private String email;

    @Size(min = 4, message = "비밀 번호를 4자 이상 입력해주세요")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
