package wooteco.subway.service.member.dto;

public class FavoriteCreateRequest {
    private Long startStationId;
    private Long endStationId;

    private FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(Long startStationId, Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
