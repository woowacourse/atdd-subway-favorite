package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UpdateMemberRequest {
    @NotEmpty(message = "이름이 입력되지 않았습니다.")
    @Pattern(regexp = "^\\S+$", message = "이름에 공백이 포함될 수 없습니다.")
    private String name;

    @NotEmpty(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(regexp = "^\\S+$", message = "비밀번호에 공백이 포함될 수 없습니다.")
    private String password;

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
