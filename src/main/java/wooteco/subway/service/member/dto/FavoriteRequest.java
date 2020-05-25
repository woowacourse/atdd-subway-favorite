package wooteco.subway.service.member.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {
    private Long sourceStationId;
    private Long targetStationId;

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

    public Favorite toFavorite() {
        return new Favorite(sourceStationId, targetStationId);
    }
}
