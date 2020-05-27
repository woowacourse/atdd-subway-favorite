package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotNull;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {
    @NotNull(message = "출발역을 입력해야 합니다.")
    private Long sourceStationId;

    @NotNull(message = "도착역을 입력해야 합니다.")
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
