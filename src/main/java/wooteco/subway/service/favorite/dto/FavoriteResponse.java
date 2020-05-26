package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
    private final Long id;
    private final String source;
    private final String target;

    public FavoriteResponse(Long id, String source, String target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
