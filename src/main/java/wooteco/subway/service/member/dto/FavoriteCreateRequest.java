package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;

public class FavoriteCreateRequest {
    private Long departStationId;
    private Long arriveStationId;

    private FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(Long departStationId, Long arriveStationId) {
        this.departStationId = departStationId;
        this.arriveStationId = arriveStationId;
    }

    public Favorite toEntity() {
        return new Favorite(departStationId, arriveStationId);
    }

    public Long getDepartStationId() {
        return departStationId;
    }

    public Long getArriveStationId() {
        return arriveStationId;
    }
}
