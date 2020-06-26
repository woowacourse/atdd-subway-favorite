package wooteco.subway.domain.line;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class LineStation {
    // 변수 타입에 관해서 겪은 문제
    //
    // 기본형은 not null이 붙고, Long은 not null이 붙지 않는다.
    // (preStationId는 null이 될 수 있어야 하기에 long 사용 x)
    //
    // Line의 LineStatoins에 삭제작업을 한 후 업데이트를 할 때 not null인 칼럼을 이용한다
    // 기존에 distance와 duration만 not null이었으므로 이들을 이용해 업데이트가 일어난다
    // 그래서 쿼리가
    // delete ~~ where distance = ~~, duration ~~ 이런 식으로 의도와 다르게 날아간다
    //
    // 일단 stationId를 long으로 수정해서 원하지 않는 동작을 방지했다
    private Long preStationId;
    private long stationId;
    private int distance;
    private int duration;

    protected LineStation() {
    }

    public LineStation(Long preStationId, Long stationId, int distance, int duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }

    public void updatePreLineStation(Long preStationId) {
        this.preStationId = preStationId;
    }

    public boolean isLineStationOf(Long preStationId, Long stationId) {
        return this.preStationId == preStationId && this.stationId == stationId
                || this.preStationId == stationId && this.stationId == preStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineStation that = (LineStation) o;
        return distance == that.distance &&
                duration == that.duration &&
                Objects.equals(preStationId, that.preStationId) &&
                Objects.equals(stationId, that.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(preStationId, stationId, distance, duration);
    }
}
