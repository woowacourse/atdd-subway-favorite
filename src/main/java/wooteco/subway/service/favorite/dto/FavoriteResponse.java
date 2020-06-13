package wooteco.subway.service.favorite.dto;

import java.util.ArrayList;
import java.util.List;

import wooteco.subway.domain.station.Station;

public class FavoriteResponse {
    private String departure;
    private String arrival;

    private FavoriteResponse() {
    }

    public FavoriteResponse(String departure, String arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    public static List<FavoriteResponse> listOf(List<Station> departures, List<Station> arrivals) {
        if (departures.size() != arrivals.size()) {
            throw new IllegalArgumentException("출발역과 도착역 정보가 쌍을 이루지 않습니다.");
        }

        List<FavoriteResponse> responses = new ArrayList<>();

        for (int i = 0; i < departures.size(); i++) {
            FavoriteResponse favoriteResponse = new FavoriteResponse(departures.get(i).getName(),
                arrivals.get(i).getName());
            responses.add(favoriteResponse);
        }

        return responses;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }
}
