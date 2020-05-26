package wooteco.subway.web.service.favorite.dto;

import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
    private Long sourceId;
    private Long targetId;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getSourceId(), favorite.getTargetId());
    }

    public static Set<FavoriteResponse> setOf(Set<Favorite> favorites) {
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toSet());
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }
}
