package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.station.Station;

public class FavoriteResponse {
    private Station preStation;
    private Station station;

    public FavoriteResponse(Station preStation, Station station) {
        this.preStation = preStation;
        this.station = station;
    }

    public Station getPreStation() {
        return preStation;
    }

    public Station getStation() {
        return station;
    }
}
