package wooteco.subway.service.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
    private Long id;
    private String sourceStation;
    private String targetStation;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final String sourceStation, final String targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public FavoriteResponse(final Long id, final String sourceStation, final String targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }
}
