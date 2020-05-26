package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.Favorite;

import java.util.Set;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private Long startStationId;
    private Long endStationId;

    public FavoriteResponse() {}

    public FavoriteResponse(Long id, Long startStationId, Long endStationId) {
        this.id = id;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getStartStationId(), favorite.getEndStationId());
    }

    public static Set<FavoriteResponse> setOf(Set<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
