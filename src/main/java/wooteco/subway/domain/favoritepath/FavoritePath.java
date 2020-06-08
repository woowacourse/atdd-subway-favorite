package wooteco.subway.domain.favoritepath;

import org.springframework.data.annotation.Id;
import wooteco.subway.domain.station.Station;

import java.util.Objects;

public class FavoritePath {
    @Id
    private Long id;
    private Long startStationId;
    private String startStationName;
    private Long endStationId;
    private String endStationName;

    public FavoritePath() {
    }

    public FavoritePath(Station start, Station end) {
        this.startStationId = start.getId();
        this.startStationName = start.getName();
        this.endStationId = end.getId();
        this.endStationName = end.getName();
    }

    public Long getId() {
        return id;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public boolean match(Station start, Station end) {
        return this.startStationId.equals(start.getId())
            && this.endStationId.equals(end.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoritePath that = (FavoritePath) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(startStationId, that.startStationId) &&
            Objects.equals(endStationId, that.endStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startStationId, endStationId);
    }
}
