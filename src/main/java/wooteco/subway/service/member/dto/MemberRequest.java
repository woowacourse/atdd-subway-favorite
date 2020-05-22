package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;

public class MemberRequest {
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;
    private String name;
    private String password;

    private MemberRequest() {
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

}
