package wooteco.subway.domain.member;

import wooteco.subway.domain.station.Station;

public class FavoriteDetail {
    private final Long memberId;
    private final Long sourceId;
    private final Long targetId;
    private final String sourceName;
    private final String targetName;

    public FavoriteDetail(Long memberId, Long sourceId, Long targetId, String sourceName,
            String targetName) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public static FavoriteDetail of(Favorite favorite) {
        Member member = favorite.getMember();
        Station source = favorite.getSource();
        Station target = favorite.getTarget();

        return new FavoriteDetail(member.getId(), source.getId(), target.getId(), source.getName(),
                target.getName());
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
