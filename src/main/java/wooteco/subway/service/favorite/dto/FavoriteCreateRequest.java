package wooteco.subway.service.favorite.dto;

public class FavoriteCreateRequest {
    private String sourceStationName;
    private String targetStationName;

    public FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(String sourceStationName, String targetStationName) {
        this.sourceStationName = sourceStationName;
        this.targetStationName = targetStationName;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getTargetStationName() {
        return targetStationName;
    }
}
