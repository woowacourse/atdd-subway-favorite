package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

import javax.validation.constraints.NotEmpty;

public class MemberRequest {
    @NotEmpty
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    public MemberRequest() {
    }

    public MemberRequest(final String email, final String name, final String password) {
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
