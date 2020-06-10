package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotEmpty;

public class UpdateMemberRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
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
