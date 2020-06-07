package wooteco.subway.service.favorite.dto;

import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteResponse {
    private final Long memberId;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(Long memberId, StationResponse source, StationResponse target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getMemberId() {
        return memberId;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
