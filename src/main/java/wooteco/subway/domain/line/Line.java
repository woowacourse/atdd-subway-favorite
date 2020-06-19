package wooteco.subway.domain.line;

import java.time.LocalTime;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.common.BaseEntity;
import wooteco.subway.domain.linestation.LineStation;
import wooteco.subway.domain.linestation.LineStations;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AttributeOverride(name = "id", column = @Column(name = "LINE_ID"))
public class Line extends BaseEntity {

    @Column(name = "LINE_NAME")
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @Embedded
    private LineStations stations = LineStations.empty();

    public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        this(name, startTime, endTime, intervalTime, LineStations.empty());
        super.id = id;
    }

    public static Line of(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        return new Line(name, startTime, endTime, intervalTime, LineStations.empty());
    }

    public void update(Line line) {
        if (line.getName() != null) {
            this.name = line.getName();
        }
        if (line.getStartTime() != null) {
            this.startTime = line.getStartTime();
        }
        if (line.getEndTime() != null) {
            this.endTime = line.getEndTime();
        }
        if (line.getIntervalTime() != 0) {
            this.intervalTime = line.getIntervalTime();
        }
    }

    public void removeLineStationById(Long stationId) {
        stations.removeById(stationId);
    }

    public List<LineStation> getStations() {
        return stations.getStations();
    }

    public List<Long> getStationIds() {
        return stations.getStationIds();
    }
}
