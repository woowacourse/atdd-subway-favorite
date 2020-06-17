package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateMemberRequest {

    @NotNull(message = "이름을 입력해주세요!")
    private String name;
    @Size(min = 10, message = "패스워드는 최소 10자를 입력해주셔야 합니다!")
    @NotNull(message = "패스워드를 입력해주세요!")
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
