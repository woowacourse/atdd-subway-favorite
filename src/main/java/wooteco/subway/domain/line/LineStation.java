package wooteco.subway.domain.line;

import java.util.Objects;

public class LineStation {
	private final Long preStationId;
	private final Long stationId;
	private final int distance;
	private final int duration;

	LineStation(Long preStationId, Long stationId, int distance, int duration) {
		this.preStationId = preStationId;
		this.stationId = stationId;
		this.distance = distance;
		this.duration = duration;
	}

	public static LineStation of(Long preStationId, Long stationId, int distance, int duration) {
		return new LineStation(preStationId, stationId, distance, duration);
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

	public LineStation updatePreLineStation(Long preStationId) {
		return new LineStation(preStationId, this.stationId, this.distance, this.duration);
	}

	public boolean isLineStationOf(Long preStationId, Long stationId) {
		return this.preStationId == preStationId && this.stationId == stationId
			|| this.preStationId == stationId && this.stationId == preStationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		LineStation that = (LineStation)o;
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
