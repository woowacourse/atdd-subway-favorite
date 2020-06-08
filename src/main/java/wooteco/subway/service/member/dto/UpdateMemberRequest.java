package wooteco.subway.service.member.dto;

@FieldMatch(field = "password", other = "confirmPassword", message = "비밀번호와 비밀번호 확인란에 적은 비밀번호가 다르면 안됩니다!")
public class UpdateMemberRequest {
    private String name;
    private String password;
    private String confirmPassword;

    public UpdateMemberRequest() {
    }

    public UpdateMemberRequest(String name, String password, String confirmPassword) {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
