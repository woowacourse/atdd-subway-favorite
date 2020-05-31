package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import java.util.LinkedHashSet;
import java.util.Set;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private Set<Favorite> favorites = new LinkedHashSet<>();

    public Member() {
    }

    public Member(Long id, String email, String name, String password) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member(String email, String name, String password) {
        this(null, email, name, password);
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
        if (favorites.contains(favorite)) {
            throw new IllegalArgumentException("이미 존재하는 즐겨찾기입니다.");
        }
        this.favorites.add(favorite);
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
        return favorites;
    }

    public void removeFavorite(Favorite favorite) {
        if (!favorites.contains(favorite)) {
            throw new IllegalArgumentException("존재하지 않는 즐겨찾기입니다.");
        }
        favorites.remove(favorite);
    }
}
