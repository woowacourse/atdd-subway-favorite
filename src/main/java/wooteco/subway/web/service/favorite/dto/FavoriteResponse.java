package wooteco.subway.web.service.favorite.dto;

import java.util.Set;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private Long sourceId;
    private Long targetId;

    protected FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long memberId, Long sourceId, Long targetId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMemberId(),
            favorite.getSourceId(),
            favorite.getTargetId());
    }

    public static Set<FavoriteResponse> setOf(Set<Favorite> favorites) {
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toSet());
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
}
