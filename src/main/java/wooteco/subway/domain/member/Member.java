package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.favorite.FavoriteStations;

import java.util.Set;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;

    @Embedded.Empty
    private FavoriteStations favoriteStations = FavoriteStations.createEmpty();

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

    public void addFavoriteStation(FavoriteStation favoriteStation) {
        favoriteStations.add(favoriteStation);
    }

    public Set<FavoriteStation> getFavoriteStations() {
        return favoriteStations.getFavorites();
    }

    public FavoriteStation findByNames(Long source, Long target) {
        return favoriteStations.findByNames(source, target);
    }

    public void deleteFavoriteStation(FavoriteStation favoriteStation) {
        favoriteStations.deleteFavoriteStation(favoriteStation);
    }

    public boolean contain(FavoriteStation favoriteStation) {
        return favoriteStations.contain(favoriteStation);
    }
}
