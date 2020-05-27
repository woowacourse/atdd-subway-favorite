package wooteco.subway.service.favorite.dto;

public class FavoriteReadRequest {
    private String sourceStationName;
    private String targetStationName;

    public FavoriteReadRequest() {
    }

    public FavoriteReadRequest(String sourceStationName, String targetStationName) {
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
