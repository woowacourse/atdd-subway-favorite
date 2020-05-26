package wooteco.subway.domain.member;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.web.exception.DuplicatedFavoriteException;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private Set<Favorite> favorites = new LinkedHashSet<>();

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

    public Member(Long id, String email, String name, String password,
        Set<Favorite> favorites) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.favorites = favorites;
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
        return favorites;
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

    public void addFavorite(Favorite favorite) {
        if (favorites.contains(favorite)) {
            throw new DuplicatedFavoriteException("해당 경로는 이미 추가되어 있습니다.");
        }
        favorites.add(favorite);
    }

    public void deleteFavorite(Long id) {
        favorites.stream()
            .filter(favorite -> favorite.isSameId(id))
            .findFirst()
            .ifPresent(favorites::remove);
    }
}
