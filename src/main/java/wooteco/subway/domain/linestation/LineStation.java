package wooteco.subway.domain.linestation;

import static javax.persistence.FetchType.*;

import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.common.BaseEntity;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "LINE_STATION_ID"))
public class LineStation extends BaseEntity {

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "PRE_STATION_ID")
    private Station preStation;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "NEXT_STATION_ID")
    private Station nextStation;

    private int distance;
    private int duration;

    @ManyToOne
    @JoinColumn(name = "LINE_ID")
    private Line line;

    @Builder
    public LineStation(Station preStation, Station nextStation, int distance, int duration) {
        this.preStation = preStation;
        this.nextStation = nextStation;
        this.distance = distance;
        this.duration = duration;
    }

    public boolean isLineStationOf(Long finalPreStationId, Long stationId) {
        return preStation.isSameId(finalPreStationId) && nextStation.isSameId(stationId);
    }

    public void applyLine(Line line) {
        if (Objects.nonNull(this.line)) {
            this.line.getStations().remove(this);
        }
        this.line = line;
        line.getStations().add(this);
    }

    public void updatePreLineStation(Station newPreStation) {
        this.preStation = newPreStation;
    }

    public Long getId() {
        return nextStation.getId();
    }
}
