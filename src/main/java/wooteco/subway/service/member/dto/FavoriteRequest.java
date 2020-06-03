package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;

public class FavoriteRequest {
    Long sourceId;
    Long targetId;

    private FavoriteRequest() {
    }

    public FavoriteRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Favorite toFavorite() {
        return new Favorite(sourceId, targetId);
    }
}
