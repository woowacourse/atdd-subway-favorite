package wooteco.subway.service.favorite.dto;

public class FavoriteRequest {
    private Long preStationId;
    private Long stationId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long preStationId, Long stationId) {
        this.preStationId = preStationId;
        this.stationId = stationId;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
