package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;

public class FavoriteRequest {
    @NotNull
    private Long departureId;
    @NotNull
    private Long arrivalId;

    private FavoriteRequest() {
    }

    public FavoriteRequest(Long departureId, Long arrivalId) {
        this.departureId = departureId;
        this.arrivalId = arrivalId;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }
}
