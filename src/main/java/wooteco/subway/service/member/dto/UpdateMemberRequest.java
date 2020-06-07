package wooteco.subway.service.member.dto;

public class UpdateMemberRequest {
    private String name;
    private String password;
    private String newPassword;

    public UpdateMemberRequest() {
    }

    public UpdateMemberRequest(String name, String password, String newPassword) {
        this.name = name;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
