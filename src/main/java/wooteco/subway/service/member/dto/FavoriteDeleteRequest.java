package wooteco.subway.service.member.dto;

public class FavoriteDeleteRequest {
    private String sourceName;
    private String targetName;

    private FavoriteDeleteRequest() {
    }

    public FavoriteDeleteRequest(String sourceName, String targetName) {
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }
}
