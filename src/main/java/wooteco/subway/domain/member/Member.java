package wooteco.subway.domain.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import wooteco.subway.domain.favorite.Favorite;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    @MappedCollection
    private List<Favorite> favorites = new ArrayList<>();

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

    public void update(String name, String password) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
    }

    public void addFavorite(Favorite favorite) {
        Objects.requireNonNull(favorite);
        favorites.add(favorite);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Favorite findEqualFavoriteTo(Favorite favorite) {
        return favorites.stream()
            .filter(it -> it.getPreStation().equals(favorite.getPreStation()))
            .filter(it -> it.getStation().equals(favorite.getStation()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 즐겨찾기가 없습니다."));
    }

    // Todo: 메서드 네이밍?
    public List<Long> getStationIdsFromFavorites() {
        return favorites.stream()
            .flatMap(it -> Stream.of(it.getPreStation(), it.getStation()))
            .collect(Collectors.toList());
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

    public List<Favorite> getFavorites() {
        return favorites;
    }
}
