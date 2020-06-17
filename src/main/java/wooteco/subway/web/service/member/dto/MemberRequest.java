package wooteco.subway.web.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import wooteco.subway.domain.member.Member;
import wooteco.subway.web.prehandler.validator.DuplicateCheck;
import wooteco.subway.web.prehandler.validator.PasswordMatch;

@PasswordMatch(
    field = "password",
    fieldMatch = "passwordCheck"
)
public class MemberRequest {

    @Email
    @DuplicateCheck
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String passwordCheck;

    public MemberRequest() {
    }

    public MemberRequest(String email, String name, String password, String passwordCheck) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }

    public Member toMember() {
        return Member.of(name, email, password);
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
