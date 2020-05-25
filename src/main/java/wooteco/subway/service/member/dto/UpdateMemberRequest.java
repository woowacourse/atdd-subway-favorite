package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;

public class UpdateMemberRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    public UpdateMemberRequest() {
    }

    public UpdateMemberRequest(String name, String oldPassword, String newPassword) {
        this.name = name;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getName() {
        return name;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
