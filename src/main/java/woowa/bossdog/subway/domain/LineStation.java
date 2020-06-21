package woowa.bossdog.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineStation {

    private Long preStationId;
    private Long stationId;
    private int distance;
    private int duration;

    public LineStation(final Long preStationId, final Long stationId, final int distance, final int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public void updatePreStation(final Long stationId) {
        this.preStationId = stationId;
    }

    public boolean isStart() {
        return preStationId == null;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LineStation that = (LineStation) o;
        return getDistance() == that.getDistance() &&
                getDuration() == that.getDuration() &&
                Objects.equals(getPreStationId(), that.getPreStationId()) &&
                Objects.equals(getStationId(), that.getStationId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPreStationId(), getStationId(), getDistance(), getDuration());
    }

    @Override
    public String toString() {
        return "LineStation{" +
                "preStationId=" + preStationId +
                ", stationId=" + stationId + "}";
    }
}
