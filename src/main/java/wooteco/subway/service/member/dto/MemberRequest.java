package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MemberRequest {
    @Email(message = "올바른 이메일 형식을 입력해주세요")
    @NotBlank
    private String email;
    @NotBlank(message = "이름에 빈값을 넣지 마세요.")
    private String name;
    @Size(min = 4, message = "비밀 번호를 4자 이상 입력해주세요")
    private String password;

    public MemberRequest() {
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
