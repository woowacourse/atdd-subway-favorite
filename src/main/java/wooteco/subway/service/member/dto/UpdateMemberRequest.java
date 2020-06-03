package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;

public class UpdateMemberRequest {
    @NotBlank(message = "이름은 빈값이 될수 없습니다.")
    private String name;
    @NotBlank(message = "비밀번호는 빈값이 될수 없습니다.")
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
