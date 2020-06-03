package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import wooteco.subway.domain.favorite.Favorite;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private Set<Favorite> favorites;
    private Role role;

    public Member() {
    }

    public Member(String email, String name, String password) {
        this(null, email, name, password);
    }

    public Member(Long id, String email, String name, String password) {
        this(id, email, name, password, new HashSet<>(), Role.USER);
    }

    public Member(Long id, String email, String name, String password,
            Set<Favorite> favorites, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.favorites = favorites;
        this.role = role;
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
        return favorites;
    }

    public Role getRole() {
        return role;
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

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public boolean hasFavorite(Favorite favorite) {
        return favorites.contains(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }
}
