package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.Objects;

public class Favorite {
    @Id
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(Long id, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this(null, sourceStationId, targetStationId);
    }

    public FavoriteResponse toFavoriteResponse() {
        return new FavoriteResponse(id, sourceStationId, targetStationId);
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) &&
                Objects.equals(sourceStationId, favorite.sourceStationId) &&
                Objects.equals(targetStationId, favorite.targetStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceStationId, targetStationId);
    }
}
