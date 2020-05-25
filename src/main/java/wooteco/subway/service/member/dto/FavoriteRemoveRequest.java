package wooteco.subway.service.member.dto;

public class FavoriteRemoveRequest {
    private Long departStationId;
    private Long arriveStationId;

    private FavoriteRemoveRequest() {
    }

    public FavoriteRemoveRequest(Long departStationId, Long arriveStationId) {
        this.departStationId = departStationId;
        this.arriveStationId = arriveStationId;
    }

    public Long getDepartStationId() {
        return departStationId;
    }

    public Long getArriveStationId() {
        return arriveStationId;
    }
}
