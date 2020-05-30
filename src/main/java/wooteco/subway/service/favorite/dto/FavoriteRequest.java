package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Favorite toFavorite(Long memberId) {
        return Favorite.of(memberId, source, target);
    }
}
