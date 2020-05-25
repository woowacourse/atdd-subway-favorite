package wooteco.subway.domain.favoritepath;

import org.springframework.data.annotation.Id;
import wooteco.subway.domain.station.Station;

public class FavoritePath {
    @Id
    private Long id;
    private Long startStationId;
    private Long endStationId;

    public FavoritePath(Station start, Station end) {
        this.startStationId = start.getId();
        this.endStationId = end.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
