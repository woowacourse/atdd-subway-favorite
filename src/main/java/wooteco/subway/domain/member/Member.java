package wooteco.subway.domain.member;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.Favorites;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY)
    private Favorites favorites;

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

    public Member(Long id, String email, String name, String password, Favorites favorites) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.favorites = favorites;
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

    public Set<Favorite> getFavorites() {
        return favorites.getFavorites();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Member member = (Member)o;
        return Objects.equals(id, member.id) &&
            Objects.equals(email, member.email) &&
            Objects.equals(name, member.name) &&
            Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password);
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", password='" + password + '\'' +
            '}';
    }

    public Set<Long> findAllFavoriteStationIds() {
        return favorites.findAllIds();
    }

    public boolean hasFavorite(Favorite favorite) {
        return favorites.hasFavorite(favorite);
    }

    public void addFavorite(Favorite favorite) {
        favorites.addFavorite(favorite);
    }
}
