package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;

public class MemberRequest {
    private String email;
    private String name;
    private String password;

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
