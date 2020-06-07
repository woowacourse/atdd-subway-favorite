package wooteco.subway.domain.member;


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import wooteco.subway.domain.favoritepath.FavoritePath;
import wooteco.subway.domain.station.Station;

public class Member {
    public static final String NOT_ME_MESSAGE = "본인의 이메일이 아닙니다.";
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    @Column("member_id")
    private Set<FavoritePath> favoritePaths = new HashSet<>();

    public Member() {
    }

    public Member(String email, String name, String password) {
        validate(email, name, password);
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
        validate(name, password);

        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
    }

    private void validate(String name, String password) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 비어있습니다.");
        }
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addFavoritePath(FavoritePath favoritePath) {
        this.favoritePaths.add(favoritePath);
    }

    public boolean hasNotId() {
        return Objects.isNull(this.getId());
    }

    public Set<FavoritePath> getFavoritePaths() {
        return Collections.unmodifiableSet(favoritePaths);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id) &&
            Objects.equals(email, member.email) &&
            Objects.equals(name, member.name) &&
            Objects.equals(password, member.password) &&
            Objects.equals(favoritePaths, member.favoritePaths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password, favoritePaths);
    }

    public void removeFavoritePath(Station start, Station end) {
        // set 에서 favorite path 를 찾아서 remove
        this.favoritePaths.removeIf(favoritePath -> favoritePath.match(start, end));
    }

    public boolean isNotMe(String email, String password) {
        return !this.email.equals(email) || !this.password.equals(password);
    }

    public boolean has(FavoritePath favoritePath) {
        return this.favoritePaths.contains(favoritePath);
    }
}
