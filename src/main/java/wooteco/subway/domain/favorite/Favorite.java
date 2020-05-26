package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

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
}
