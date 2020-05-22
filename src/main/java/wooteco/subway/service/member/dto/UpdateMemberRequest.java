package wooteco.subway.service.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import wooteco.subway.web.member.PasswordMatch;

@PasswordMatch(
    field = "password",
    fieldMatch = "passwordCheck"
)
public class UpdateMemberRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordCheck;

    public UpdateMemberRequest() {
    }

    public UpdateMemberRequest(String email, String name, String oldPassword, String password,
        String passwordCheck) {
        this.email = email;
        this.name = name;
        this.oldPassword = oldPassword;
        this.password = password;
        this.passwordCheck = passwordCheck;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordCheck() {
        return passwordCheck;
    }
}
