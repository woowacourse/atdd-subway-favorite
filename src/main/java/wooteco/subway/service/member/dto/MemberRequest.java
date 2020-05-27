package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import wooteco.subway.domain.member.Member;

public class MemberRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일형식을 지켜주세요.")
    private String email;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    private MemberRequest() {
    }

    public MemberRequest(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

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
