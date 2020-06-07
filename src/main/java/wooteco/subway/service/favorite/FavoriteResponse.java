package wooteco.subway.service.favorite;

import wooteco.subway.domain.favorite.FavoriteStation;

public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private Long sourceId;
    private Long targetId;
    private String sourceName;
    private String targetName;

    public FavoriteResponse() {
    }

    public FavoriteResponse(FavoriteStation favoriteStation, String sourceName, String targetName) {
        this.id = favoriteStation.getId();
        this.memberId = favoriteStation.getMemberId();
        this.sourceId = favoriteStation.getSourceId();
        this.targetId = favoriteStation.getTargetId();
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
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
