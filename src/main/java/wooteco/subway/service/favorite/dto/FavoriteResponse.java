package wooteco.subway.service.favorite.dto;

import static java.util.stream.Collectors.*;

import java.util.List;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
    private String departure;
    private String arrival;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String departure, String arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getDeparture(), favorite.getArrival());
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(toList());
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }
}
