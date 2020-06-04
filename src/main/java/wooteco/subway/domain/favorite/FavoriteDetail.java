package wooteco.subway.domain.favorite;

public class FavoriteDetail {
    private Long sourceId;
    private Long targetId;
    private String sourceName;
    private String targetName;

    public FavoriteDetail(Long sourceId, Long targetId, String sourceName, String targetName) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }
}
