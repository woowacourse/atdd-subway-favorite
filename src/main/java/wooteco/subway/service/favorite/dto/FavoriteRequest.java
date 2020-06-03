package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.Positive;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {
    @Positive(message = "시작역의 id값이 빈 값이 들어갈 수 없습니다.")
    private Long sourceStationId;
    @Positive(message = "도착역의 id값이 빈 값이 들어갈 수 없습니다.")
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
