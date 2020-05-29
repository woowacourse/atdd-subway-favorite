package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.member.Favorite;

public class FavoriteResponse {
    private Long id;
    private Long departureId;
    private Long destinationId;

    private FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long departureId, Long destinationId) {
        this.id = id;
        this.departureId = departureId;
        this.destinationId = destinationId;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getDepartureId(),
                favorite.getDestinationId());
    }

    public Long getId() {
        return id;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getDestinationId() {
        return destinationId;
    }
}
