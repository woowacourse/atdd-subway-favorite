package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotBlank;

public class FavoriteCreateRequest {
    @NotBlank
    private String departure;
    @NotBlank
    private String arrival;

    private FavoriteCreateRequest() {
    }

    public FavoriteCreateRequest(String departure, String arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }
}
