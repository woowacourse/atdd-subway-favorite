package wooteco.subway.service.member.dto;

public class FavoriteDeleteRequest {
    private Long sourceStationId;
    private Long targetStationId;

    private FavoriteDeleteRequest() {
    }

    public FavoriteDeleteRequest(final Long sourceStationId, final Long targetStationId) {
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
