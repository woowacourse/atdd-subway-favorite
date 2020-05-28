package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotEmpty;

public class UpdateMemberRequest {
    @NotEmpty(message = "이름이 비었습니다.")
    private String name;
    @NotEmpty(message = "패스워드가 비었습니다.")
    private String password;

    public UpdateMemberRequest() {
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
