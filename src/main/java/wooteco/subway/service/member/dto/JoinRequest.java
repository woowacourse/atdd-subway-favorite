package wooteco.subway.service.member.dto;

public class JoinRequest {
    private String email;
    private String name;
    private String password;
    private String passwordCheck;

    public JoinRequest(String email, String name, String password, String passwordCheck) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.passwordCheck = passwordCheck;
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

    public String getPasswordCheck() {
        return passwordCheck;
    }
}
