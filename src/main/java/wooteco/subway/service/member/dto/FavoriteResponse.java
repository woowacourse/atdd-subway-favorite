package wooteco.subway.service.member.dto;

import wooteco.subway.domain.station.Station;

public class FavoriteResponse {
    private Long id;
    private Station startStation;
    private Station endStation;

    public FavoriteResponse() {}

    public FavoriteResponse(Long id, Station startStation, Station endStation) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public static FavoriteResponse of(Long id, Station startStation, Station endStation) {
        return new FavoriteResponse(id, startStation, endStation);
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public Long getId() {
        return id;
    }
}
