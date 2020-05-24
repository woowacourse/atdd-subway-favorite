package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteRequest {
    private String source;
    private String target;

    private FavoriteRequest() {
    }

    public FavoriteRequest(final String source, final String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Favorite toFavorite(final Long memberId, final Long sourceStationId, final Long targetStationId) {
        return new Favorite(memberId, sourceStationId, targetStationId);
    }
}
