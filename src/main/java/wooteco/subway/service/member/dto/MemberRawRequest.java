package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.member.DuplicateCheck;
import wooteco.subway.web.member.PasswordMatch;

@PasswordMatch(
    field = "password",
    fieldMatch = "passwordCheck"
)
public class MemberRawRequest {

    @Email @DuplicateCheck @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
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
