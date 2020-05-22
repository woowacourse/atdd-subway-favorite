package wooteco.subway.domain.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Embedded;
import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.domain.member.favorite.Favorites;

public class Member {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;

    @Embedded.Empty
    private Favorites favorites = Favorites.empty();

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

    @PersistenceConstructor
    public Member(final Long id, final String email, final String name, final String password, final Favorites favorites) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.favorites = favorites;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void addFavorite(Favorite favorite) {
        favorites.add((favorite));
    }

    public void removeFavorite(Favorite favorite) {
        favorites.remove(favorite);
    }

    public void update(String name, String password) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
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

    public Favorites getFavorites() {
        return favorites;
    }
}
