package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import wooteco.subway.domain.member.Member;

public class MemberRequest {
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "")
    private String name;
    @Size(min = 5, max = 19)
    private String password;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Member toMember() {
        return new Member(email, name, password);
    }
}
