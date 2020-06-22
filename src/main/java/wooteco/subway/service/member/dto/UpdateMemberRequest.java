package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;

public class UpdateMemberRequest {
    @NotBlank(message = "NAME_EMPTY")
    private String name;
    @NotBlank(message = "PASSWORD_EMPTY")
    private String password;

    private UpdateMemberRequest() {
    }

    public UpdateMemberRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
