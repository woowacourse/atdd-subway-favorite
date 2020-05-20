package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;

public class MemberRequest {
    @Email
    private String email;
    private String name;
    private String password;

    private MemberRequest() {
    }

    public MemberRequest(@Email final String email, final String name, final String password) {
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

}
