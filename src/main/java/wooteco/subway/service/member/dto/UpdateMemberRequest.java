package wooteco.subway.service.member.dto;

public class UpdateMemberRequest {
    private String name;
    private String email;
    private String password;

    public UpdateMemberRequest() {
    }

    public UpdateMemberRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
