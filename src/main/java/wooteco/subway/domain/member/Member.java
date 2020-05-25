package wooteco.subway.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import wooteco.subway.domain.path.StationPath;
import wooteco.subway.exceptions.DuplicatedFavoritePathException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Member {
    @Id
    private Long id;
    private String email;
    private String name;
    private String password;
    private List<StationPath> favoritePaths = new ArrayList<>();

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

    public Member(Long id, String email, String name, String password, List<StationPath> favoritePaths) {
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

    public void addFavoritePath(StationPath stationPath) {
        validateDuplication(stationPath);

        if (Objects.nonNull(stationPath)) {
            this.favoritePaths.add(stationPath);
        }
    }

    private void validateDuplication(StationPath stationPath) {
        favoritePaths.stream()
                .filter(path -> Objects.equals(path.getSourceId(), stationPath.getSourceId()) &&
                        Objects.equals(path.getTargetId(), stationPath.getTargetId()))
                .findFirst()
                .ifPresent(path -> {
                    throw new DuplicatedFavoritePathException();
                });
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean hasNotPath(Long pathId) {
        return favoritePaths.stream()
                .noneMatch(path -> Objects.equals(path.getId(), pathId));
    }

    public void deletePath(Long pathId) {
        favoritePaths = favoritePaths.stream()
                .filter(path -> !Objects.equals(path.getId(), pathId))
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

    public List<StationPath> getFavoritePaths() {
        return favoritePaths;
    }

    public StationPath getRecentlyUpdatedPath() {
        return favoritePaths.get(favoritePaths.size() - 1);
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
