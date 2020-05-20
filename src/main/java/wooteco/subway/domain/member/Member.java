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
        validate(email, name, password);
        this.email = email;
        this.name = name;
        this.password = password;
    }

    private void validate(String email, String name, String password) {
        validateEmail(email);
        validateName(name);
        validatePassword(password);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new MemberConstructException(MemberConstructException.EMPTY_NAME_MESSAGE);
        }
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new MemberConstructException(MemberConstructException.EMPTY_PASSWORD_MESSAGE);
    }
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new MemberConstructException(MemberConstructException.EMPTY_EMAIL_MESSAGE);
        }
        if (!email.contains("@")) {
            throw new MemberConstructException(MemberConstructException.INVALID_EMAIL_FORM_MESSAGE);
        }
    }

    public Member(Long id, String email, String name, String password) { // Todo 없애볼것
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
}
