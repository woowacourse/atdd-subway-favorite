package wooteco.subway.domain.line;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import wooteco.subway.domain.station.Station;

@Entity
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station preStation;
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;
    private int distance;
    private int duration;

    public LineStation() {
    }

    public LineStation(final Station preStation, final Station station, final int distance,
        final int duration) {
        this.preStation = preStation;
        this.station = station;
        this.distance = distance;
        this.duration = duration;
    }

    public void updatePreLineStation(Station preStation) {
        this.preStation = preStation;
    }

    public boolean isLineStationOf(Long preStationId, Long stationId) {
        return this.preStation.equals(preStationId) && this.station.equals(stationId)
            || this.preStation.equals(stationId) && this.station.equals(preStationId);
    }

    public boolean isNotFirstLineStation() {
        return preStation != null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LineStation that = (LineStation)o;
        return distance == that.distance &&
            duration == that.duration &&
            Objects.equals(preStation, that.preStation) &&
            Objects.equals(station, that.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preStation, station, distance, duration);
    }
}
