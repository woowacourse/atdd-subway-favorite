package wooteco.subway.domain.line;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.common.BaseEntity;

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

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineStation> stations = new ArrayList<>();

    public static Line of(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        return new Line(name, startTime, endTime, intervalTime, new ArrayList<>());
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

    public void addLineStation(LineStation lineStation) {
        stations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        stations = stations.stream().filter(station -> !station.isSameId(stationId))
            .collect(Collectors.toList());
    }
}
