package wooteco.subway.domain.line;

import static javax.persistence.FetchType.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.common.BaseEntity;
import wooteco.subway.domain.station.Station;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "LINE_STATION_ID"))
public class LineStation extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "PRE_STATION_ID")
    private Station preStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "NEXT_STATION_ID")
    private Station nextStation;

    private int distance;
    private int duration;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "LINE_ID")
    private Line line;

    public boolean isSameId(Long stationId) {
        return nextStation.isSameId(stationId);
    }

    public boolean isLineStationOf(Long finalPreStationId, Long stationId) {
        return preStation.isSameId(finalPreStationId) && nextStation.isSameId(stationId);
    }
}
