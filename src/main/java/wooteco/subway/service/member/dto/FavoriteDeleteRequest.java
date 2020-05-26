package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;

public class FavoriteDeleteRequest {
    private Long departStationId;
    private Long arriveStationId;

    private FavoriteDeleteRequest() {
    }

    public FavoriteDeleteRequest(Long departStationId, Long arriveStationId) {
        this.departStationId = departStationId;
        this.arriveStationId = arriveStationId;
    }

    public Long getDepartStationId() {
        return departStationId;
    }

    public Long getArriveStationId() {
        return arriveStationId;
    }

    public Favorite toEntity() {
        return new Favorite(departStationId, arriveStationId);
    }
}
