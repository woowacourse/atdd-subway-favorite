package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UpdateMemberRequest {
    @NotBlank(message = "이름이 입력되지 않았습니다.")
    @Pattern(regexp = "^\\S.*\\S$|^\\S$", message = "이름의 앞 뒤에 공백이 올 수 없습니다.")
    private String name;

    @NotEmpty(message = "비밀번호가 입력되지 않았습니다.")
    @Pattern(regexp = "^\\S+$", message = "비밀번호에 공백이 포함될 수 없습니다.")
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
