package wooteco.subway.service.member.dto;

import org.apache.naming.factory.BeanFactory;
import wooteco.subway.domain.member.Member;

import javax.validation.constraints.NotBlank;

@FieldMatch(field = "password", other = "confirmPassword", message = "비밀번호와 비밀번호 확인란에 적은 비밀번호가 다르면 안됩니다!")
public class MemberRequest {
    @NotBlank(message = "이메일은 공란이 될 수 없습니다!")
    private String email;

    @NotBlank(message = "이름은 공란이 될 수 없습니다!")
    private String name;

    @NotBlank(message = "비밀번호는 공란이 될 수 없습니다!")
    private String password;

    @NotBlank(message = "비밀번호 확인란은 공란이 될 수 없습니다!")
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public Member toMember() {
        return new Member(email, name, password);
    }
}
