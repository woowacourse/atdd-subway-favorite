package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberRequest {
    @Email(message = "이메일 형식이 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;
    @NotBlank(message = "이름을 입력하세요.")
    private String name;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;

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
