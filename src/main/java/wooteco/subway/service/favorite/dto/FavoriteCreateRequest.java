package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.member.Favorite;

public class FavoriteCreateRequest {
    private Long departureId;
    private Long destinationId;

    private FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(Long departureId, Long destinationId) {
        this.departureId = departureId;
        this.destinationId = destinationId;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public Favorite toFavorite() {
        return Favorite.of(departureId, destinationId);
    }
}
