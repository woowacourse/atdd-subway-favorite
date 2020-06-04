package wooteco.subway.domain.member;

import static wooteco.subway.exception.InvalidFavoriteException.NOT_FOUND_FAVORITE;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import wooteco.subway.exception.InvalidFavoriteException;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private Set<Favorite> favorites;

    public Member() {
    }

    public Member(String email, String name, String password) {
        this(null, email, name, password);
    }

    public Member(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.favorites = new HashSet<>();
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

    public Favorite findFavorite(Long departureId, Long destinationId) {
        return favorites.stream()
            .filter(favorite -> Objects.equals(favorite.getDepartureId(), departureId))
            .filter(favorite -> Objects.equals(favorite.getDestinationId(), destinationId))
            .findFirst()
            .orElseThrow(() -> new InvalidFavoriteException(NOT_FOUND_FAVORITE));
    }

    public void deleteFavorite(Long favoriteId) {
        Favorite favoriteToRemove = favorites.stream()
            .filter(favorite -> Objects.equals(favorite.getId(), favoriteId))
            .findFirst()
            .orElseThrow(() -> new InvalidFavoriteException(NOT_FOUND_FAVORITE));
        favorites.remove(favoriteToRemove);
    }
}