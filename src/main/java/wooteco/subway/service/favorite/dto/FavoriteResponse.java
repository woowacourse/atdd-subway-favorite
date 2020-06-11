package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.station.Station;
import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse preStation;
    private StationResponse station;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Station preStation, Station station) {
        this.id = id;
        this.preStation = StationResponse.of(preStation);
        this.station = StationResponse.of(station);
    }

    public Long getId() {
        return id;
    }

    public StationResponse getPreStation() {
        return preStation;
    }

    public StationResponse getStation() {
        return station;
    }
}
