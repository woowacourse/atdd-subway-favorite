package wooteco.subway.domain.member;

import java.util.LinkedHashSet;
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
    @Embedded.Nullable
    private Favorites favorites;

    public Member() {
    }

    public Member(String email, String name, String password) {
        this(null, email, name, password, new Favorites(new LinkedHashSet<>()));
    }

    public Member(Long id, String email, String name, String password) {
        this(id, email, name, password, new Favorites(new LinkedHashSet<>()));
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

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public void deleteFavorite(Long id) {
        favorites.delete(id);
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

    public Set<Favorite> getFavorites() {
        return favorites.getFavorites();
    }

    public String getPassword() {
        return password;
    }
}
