package wooteco.subway.service.member.dto;

public class FavoriteResponse {
    private String sourceName;
    private String targetName;

    public FavoriteResponse() {
    }

    public FavoriteResponse(String sourceName, String targetName) {
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
