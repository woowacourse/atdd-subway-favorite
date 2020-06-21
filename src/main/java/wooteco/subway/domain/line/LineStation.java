package wooteco.subway.domain.line;

import java.util.Objects;

import javax.persistence.CascadeType;
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
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "pre_station_id")
    private Station preStation;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "station_id")
    private Station station;
    private int distance;
    private int duration;

    protected LineStation() {
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
        return
            this.preStation.getId().equals(preStationId) && this.station.getId().equals(stationId)
                || this.preStation.getId().equals(stationId) && this.station.getId()
                .equals(preStationId);
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

    @Override
    public String toString() {
        return "LineStation{" +
            "id=" + id +
            ", preStation=" + preStation +
            ", station=" + station +
            '}';
    }
}
