package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import wooteco.subway.web.exceptions.InvalidRegisterException;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    @Embedded.Empty
    private Favorites favorites = new Favorites(new HashSet<>());

    public Member() {
    }

    public Member(String email, String name, String password) {
        this(null, email, name, password);
    }

    public Member(Long id, String email, String name, String password) {
        validate(email, name, password);
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    private void validate(final String email, final String name, final String password) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(name) || StringUtils.isBlank(password)) {
            throw new InvalidRegisterException("빈 값을 입력할 수 없습니다.");
        }
    }

    public void addFavorite(final Favorite favorite) {
        favorites.addFavorite(favorite);
    }

    public void removeFavorite(final Long id) {
        favorites.removeFavorite(id);
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
        return Objects.equals(this.id, id);
    }

    public boolean isAuthenticated(final String email) {
        return Objects.equals(this.email, email);

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

    public Favorites getFavorites() {
        return favorites;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Member member = (Member)o;
        return Objects.equals(id, member.id) &&
            Objects.equals(email, member.email) &&
            Objects.equals(name, member.name) &&
            Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password);
    }
}
