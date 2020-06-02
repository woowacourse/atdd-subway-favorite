package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FavoriteResponse {
    private Long departureId;
    private Long arrivalId;

    private FavoriteResponse() {
    }

    public FavoriteResponse(Long departureId, Long arrivalId) {
        this.departureId = departureId;
        this.arrivalId = arrivalId;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getDepartureId(), favorite.getArrivalId());
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(toList());
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }
}
