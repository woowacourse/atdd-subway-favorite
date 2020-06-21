package woowa.bossdog.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.service.line.dto.UpdateLineRequest;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.*;

import static javax.persistence.FetchType.LAZY;

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

    @ElementCollection(fetch = LAZY)
    @CollectionTable(name = "line_station", joinColumns = @JoinColumn(name = "line_id"))
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

    public List<Long> getStationIds() {
        final List<Long> stationIds = new ArrayList<>();
        Long stationId = null;
        if (lineStations.size() > 0) {
            LineStation start = lineStations.stream()
                    .filter(LineStation::isStart)
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
            stationId = start.getStationId();
        }

        for (int i = 0; i < lineStations.size(); i++) {
            stationIds.add(stationId);
            final Long finalStationId = stationId;
            Optional<LineStation> next = lineStations.stream()
                    .filter(ls -> Objects.equals(finalStationId, ls.getPreStationId()))
                    .findFirst();
            if (next.isPresent()) {
                stationId = next.get().getStationId();
            }
        }
        return stationIds;
    }
}
