package wooteco.subway.service.member.favorite.dto;

import wooteco.subway.domain.member.Favorite;

public class FavoriteResponse {
    private Long memberId;
    private Long sourceId;
    private Long targetId;

    public FavoriteResponse(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static FavoriteResponse of(Long memberId, Favorite favorite) {
        return new FavoriteResponse(memberId, favorite.getSourceId(), favorite.getTargetId());
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
}
