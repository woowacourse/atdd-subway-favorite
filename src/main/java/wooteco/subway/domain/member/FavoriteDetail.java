package wooteco.subway.domain.member;

public class FavoriteDetail {
    private final Long memberId;
    private final Long sourceId;
    private final Long targetId;
    private final String sourceName;
    private final String targetName;

    public FavoriteDetail(Long memberId, Long sourceId, Long targetId, String sourceName, String targetName) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public static FavoriteDetail of(Long memberId, Favorite favorite, String sourceName, String targetName) {
        return new FavoriteDetail(memberId, favorite.getSourceId(), favorite.getTargetId(), sourceName, targetName);
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
