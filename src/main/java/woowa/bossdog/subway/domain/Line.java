package woowa.bossdog.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.service.line.dto.UpdateLineRequest;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;

    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineStation> lineStations = new ArrayList<>();

    public Line(final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime) {
        this(null, name, startTime, endTime, intervalTime);
    }

    public Line(final Long id, final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public void update(final UpdateLineRequest request) {
        this.name = request.getName();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.intervalTime = request.getIntervalTime();
    }

    public List<Station> getStations() {
        final List<Station> stations = new ArrayList<>();
        Station station = null;
        if (lineStations.size() > 0) {
            LineStation start = lineStations.stream()
                    .filter(LineStation::isStart)
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
            station = start.getStation();
        }

        for (int i = 0; i < lineStations.size(); i++) {
            stations.add(station);
            final Station finalStation = station;
            Optional<LineStation> next = lineStations.stream()
                    .filter(ls -> Objects.equals(finalStation, ls.getPreStation()))
                    .findFirst();
            if (next.isPresent()) {
                station = next.get().getStation();
            }
        }
        return stations;
    }
}
