package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {

    @NotNull
    private Long sourceId;

    @NotNull
    private Long targetId;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Favorite toFavorite() {
        return new Favorite(null, sourceId, targetId);
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
