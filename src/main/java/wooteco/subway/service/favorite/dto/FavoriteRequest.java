package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {
    private Long sourceStationId;
    private Long targetStationId;

    private FavoriteRequest() {
    }

    public Favorite toFavorite() {
        return new Favorite(sourceStationId, targetStationId);
    }

    public FavoriteRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
