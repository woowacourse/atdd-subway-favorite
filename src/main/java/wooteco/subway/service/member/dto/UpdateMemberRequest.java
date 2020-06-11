package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;

public class UpdateMemberRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String password;

    public UpdateMemberRequest() {
    }

    public UpdateMemberRequest(@NotBlank String name,
        @NotBlank String password) {
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
