package wooteco.subway.domain.line;

import java.util.Objects;
import org.springframework.data.annotation.Id;

public class LineStation {

	@Id
	private final Long id;
	private Long preStationId;
	private Long stationId;
	private int distance;
	private int duration;

	private LineStation(Long id, Long preStationId, Long stationId, int distance, int duration) {
		this.id = id;
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
	}

	public static LineStation of(Long preStationId, Long stationId, int distance, int duration) {
		return new LineStation(null, preStationId, stationId, distance, duration);
	}

	public LineStation withId(Long Id) {
		return new LineStation(Id, this.preStationId, this.stationId, this.distance, this.duration);
	}

	public void updatePreLineStation(Long preStationId) {
		this.preStationId = preStationId;
	}

	public boolean isLineStationOf(Long preStationId, Long stationId) {
		return this.preStationId.equals(preStationId) && this.stationId.equals(stationId)
			|| this.preStationId.equals(stationId) && this.stationId.equals(preStationId);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		LineStation that = (LineStation) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "LineStation{" +
			"id=" + id +
			", preStationId=" + preStationId +
			", stationId=" + stationId +
			", distance=" + distance +
			", duration=" + duration +
			'}';
	}
}
