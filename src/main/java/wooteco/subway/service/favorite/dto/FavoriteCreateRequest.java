package wooteco.subway.service.favorite.dto;

public class FavoriteCreateRequest {
    private Long sourceStationId;
    private Long targetStationId;

    public FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(final Long sourceStationId, final Long targetStationId) {
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
