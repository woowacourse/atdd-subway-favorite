package wooteco.subway.service.favorite.dto;

public class FavoriteResponse {
    private String departure;
    private String arrival;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String departure, String arrival) {
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
