package wooteco.subway.service.favorite.dto;

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
}
