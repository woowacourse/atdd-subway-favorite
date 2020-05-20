package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import javax.validation.constraints.NotBlank;

public class MemberRequest {
    @NotBlank(message = "이메일을 입력하세요")
    private String email;
    @NotBlank(message = "이름을 입력하세요")
    private String name;
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
