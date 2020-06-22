package woowa.bossdog.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(name = "line_station")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineStation {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pre_station_id")
    private Station preStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    private int distance;
    private int duration;

    public LineStation(final Line line, final Station preStation, final Station station, final int distance, final int duration) {
        this(null, line, preStation, station, distance, duration);
    }

    public LineStation(final Long id, final Line line, final Station preStation, final Station station, final int distance, final int duration) {
        this.id = id;
        this.line = line;
        this.preStation = preStation;
        this.station = station;
        this.distance = distance;
        this.duration = duration;
    }

    public void updatePreStation(final Station station) {
        this.preStation = station;
    }

    public boolean isStart() {
        return Objects.isNull(preStation) || Objects.isNull(preStation.getId());
    }

    @Override
    public String toString() {
        return "LineStation{" +
                "line=" + line +
                ", preStation=" + preStation +
                ", station=" + station +
                '}';
    }
}
