package wooteco.subway.domain.line;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import wooteco.subway.domain.station.Station;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @ManyToOne
    private Station preStation;

    @ManyToOne
    private Station station;
    private int distance;
    private int duration;

    protected LineStation() {
    }

    public LineStation(Station preStation, Station station, int distance, int duration) {
        this.station = station;
        this.preStation = preStation;
        this.distance = distance;
        this.duration = duration;
    }

    public LineStation(Line line, Station preStation, Station station, int distance, int duration) {
        this.line = line;
        this.station = station;
        this.preStation = preStation;
        this.distance = distance;
        this.duration = duration;
    }

    public LineStation(Long id, Line line, Station preStation, Station station, int distance,
        int duration) {
        this.id = id;
        this.line = line;
        this.station = station;
        this.preStation = preStation;
        this.distance = distance;
        this.duration = duration;
    }

    public boolean isLineStationOf(Long preStationId, Long stationId) {
        return this.preStation.isSame(preStationId) && this.station.isSame(stationId)
            || this.preStation.isSame(stationId) && this.station.isSame(preStationId);
    }

    public Station getPreStation() {
        return preStation;
    }

    public Station getStation() {
        return station;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public void updatePreLineStation(Station station) {
        this.preStation = station;
    }

    public void setLine(Line line) {
        line.addLineStation(this);
        this.line = line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LineStation that = (LineStation)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
