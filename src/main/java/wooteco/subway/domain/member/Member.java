package wooteco.subway.domain.member;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

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

    public void addFavorite(long departureId, long destinationId) {
        favorites.add(Favorite.of(departureId, destinationId));
    }

    public Favorite findFavorite(Long departureId, Long destinationId) {
        return favorites.stream()
            .filter(favorite -> Objects.equals(favorite.getDepartureId(), departureId))
            .filter(favorite -> Objects.equals(favorite.getDestinationId(), destinationId))
            .findFirst()
            .orElseThrow(AssertionError::new);
    }
}
