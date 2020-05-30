package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Member {
    @Id
    private Long id;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
    private Set<Favorite> favorites = new HashSet<>();

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

    public Member(String email, String name, String password, Set<Favorite> favorites) {
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
        this.favorites.add(favorite);
    }

    public void removeFavorite(Favorite favorite) {
        this.favorites.remove(favorite);
    }

    public void removeFavoriteByStationId(Long id) {
        favorites = favorites.stream()
                .filter(favorite -> !favorite.getSourceStationId().equals(id)
                        && !favorite.getTargetStationId().equals(id))
                .collect(Collectors.toSet());
    }

    public boolean isNotSameUser(Member targetmember) {
        return !this.email.equals(targetmember.email);
    }
}
