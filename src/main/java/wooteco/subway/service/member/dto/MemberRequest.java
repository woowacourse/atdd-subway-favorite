package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import wooteco.subway.domain.member.Member;

public class MemberRequest {
    @Email(message = "email형식이 아닙니다.")
    private String email;
    @NotEmpty(message = "이름이 비었습니다.")
    private String name;
    @NotEmpty(message = "패스워드가 비었습니다.")
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
