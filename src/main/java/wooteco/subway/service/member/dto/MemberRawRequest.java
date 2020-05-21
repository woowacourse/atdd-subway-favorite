package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.member.DuplicateCheck;
import wooteco.subway.web.member.PasswordMatch;

@PasswordMatch(
    field = "password",
    fieldMatch = "passwordCheck"
)
public class MemberRawRequest {

    @DuplicateCheck
    private String email;
    private String name;
    private String password;
    private String passwordCheck;

    public MemberRawRequest() {
    }

    public MemberRawRequest(String email, String name, String password, String passwordCheck) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }

    public Member toMember() {
        return new Member(email, name, password);
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

    public String getPasswordCheck() {
        return passwordCheck;
    }
}
