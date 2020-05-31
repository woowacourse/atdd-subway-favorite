package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.path.FavoritePaths;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;

    @Embedded.Empty
    private FavoritePaths favoritePaths = new FavoritePaths(new ArrayList<>());

    public Member() {
    }

    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member(Long id, String email, String name, String password) {
        this(email, name, password);
        this.id = id;
    }

    public Member(Long id, String email, String name, String password, FavoritePaths favoritePaths) {
        this(id, email, name, password);
        this.favoritePaths = favoritePaths;
    }

    public void update(String name, String password) {
        if (StringUtils.isNotBlank(name)) {
            this.name = name;
        }
        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
    }

    public void addFavoritePath(FavoritePath favoritePath) {
        favoritePaths.addPath(favoritePath);
    }

    public boolean hasIdenticalPasswordWith(String password) {
        return Objects.equals(this.password, password);
    }

    public boolean hasNotPath(Long pathId) {
        return favoritePaths.hasNotPath(pathId);
    }

    public void deletePath(Long pathId) {
        favoritePaths.deletePath(pathId);
    }

    public FavoritePath getRecentlyUpdatedPath() {
        return favoritePaths.getRecentlyUpdatedPath();
    }

    public List<Long> getFavoritePathsIds() {
        return favoritePaths.getPathsIds();
    }

    public List<Long> getFavoritePathsStationsIds() {
        return favoritePaths.getStationsIds();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) &&
                Objects.equals(email, member.email) &&
                Objects.equals(name, member.name) &&
                Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password);
    }
}
