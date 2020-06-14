package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;
import wooteco.subway.domain.member.Member;

public class MemberRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String name;
    @NotBlank
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
