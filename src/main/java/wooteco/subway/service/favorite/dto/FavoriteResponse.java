package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.station.Station;

public class FavoriteResponse {
    private Long id;
    private Station preStation;
    private Station station;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Station preStation, Station station) {
        this.id = id;
        this.preStation = preStation;
        this.station = station;
    }

    public static FavoriteResponse of(Long id, Station preStation, Station station) {
        return new FavoriteResponse(id, preStation, station);
    }

    public Long getId() {
        return id;
    }

    public Station getPreStation() {
        return preStation;
    }

    public Station getStation() {
        return station;
    }
}
