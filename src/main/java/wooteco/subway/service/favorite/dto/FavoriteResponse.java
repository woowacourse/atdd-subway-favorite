package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;
    private String sourceStationName;
    private String targetStationName;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long sourceStationId, Long targetStationId, String sourceStationName, String targetStationName) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.sourceStationName = sourceStationName;
        this.targetStationName = targetStationName;
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

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getTargetStationName() {
        return targetStationName;
    }
}
