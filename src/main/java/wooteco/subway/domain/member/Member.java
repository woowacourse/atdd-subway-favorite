package wooteco.subway.domain.member;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;

    public Member() {
    }

    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Long getId() {
        return id;
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

    public void update(String name, String password) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean isAuthenticated(Long id) {
        return Objects.nonNull(this.id) && this.id == id;
    }

    public boolean isAuthenticated(final String email) {
        return Objects.nonNull(this.email) && this.email.equals(email);

    }
}
