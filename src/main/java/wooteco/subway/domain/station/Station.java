package wooteco.subway.domain.station;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import wooteco.subway.domain.favorite.Favorite;

public class Station {
    @Id
    private Long id;
    private String name;
    @CreatedDate
    private LocalDateTime createdAt;

    @MappedCollection(idColumn = "source_id")
    private Set<Favorite> favoritesSource;
    @MappedCollection(idColumn = "target_id")
    private Set<Favorite> favoritesTarget;

    public Station() {
    }

    public Station(String name) {
        this.name = name;
        this.favoritesSource = new HashSet<>();
        this.favoritesTarget = new HashSet<>();
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
        this.favoritesSource = new HashSet<>();
        this.favoritesTarget = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Set<Favorite> getFavoritesSource() {
        return favoritesSource;
    }

    public Set<Favorite> getFavoritesTarget() {
        return favoritesTarget;
    }

    @Override
    public String toString() {
        return "Station{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", createdAt=" + createdAt +
            ", favoritesSource=" + favoritesSource +
            ", favoritesTarget=" + favoritesTarget +
            '}';
    }
}
