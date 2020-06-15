package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

import java.util.Set;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;

    @Embedded.Empty
    private Favorites favorites = Favorites.empty();

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

    public Favorites getFavorites() {
        return favorites;
    }

    public void addFavorite(Favorite favorite) {
        favorites.add(favorite);
    }

    public void removeFavorite(Long favoriteId) {
        favorites.remove(favoriteId);
    }

    public void update(String name, String password) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
    }

    public Set<Long> getFavoriteStationIds() {
        return favorites.getFavoriteStationIds();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
