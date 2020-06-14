package wooteco.subway.domain.favoritepath;

import java.util.Objects;
import org.springframework.data.annotation.Id;
import wooteco.subway.domain.station.Station;

public class FavoritePath {
    @Id
    private Long id;
    private Long startStationId;
    private Long endStationId;

    public FavoritePath() {
    }

    public FavoritePath(Station start, Station end) {
        this.startStationId = start.getId();
        this.endStationId = end.getId();
    }

    public FavoritePath(Long startStationId, Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
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
