package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberRequest {
    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"
            , message = "잘못된 이메일 값입니다.")
    @NotBlank
    private String email;
    @NotBlank(message = "이름에 빈값을 넣지 마세요.")
    private String name;
    @NotBlank(message = "빈값을 넣지 마세요.")
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
