package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
