package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import wooteco.subway.domain.member.Member;

public class MemberRequest {
    @NotEmpty(message = "이메일이 입력되지 않았습니다.")
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotEmpty(message = "이름이 입력되지 않았습니다.")
    @Pattern(regexp = "^\\S+$", message = "이름에 공백이 포함될 수 없습니다.")
    private String name;

    @NotEmpty(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(regexp = "^\\S+$", message = "비밀번호에 공백이 포함될 수 없습니다.")
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
