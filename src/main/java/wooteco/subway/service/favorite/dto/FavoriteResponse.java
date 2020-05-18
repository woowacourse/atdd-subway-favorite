package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
                .map(it -> FavoriteResponse.of(it))
                .collect(Collectors.toList());
    }

    private static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSourceStationId(), favorite.getTargetStationId());
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
