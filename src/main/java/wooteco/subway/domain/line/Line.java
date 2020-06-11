package wooteco.subway.domain.line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Embedded;

public class Line {

	@Id
	private final Long id;
	private final String name;
	private final LocalTime startTime;
	private final LocalTime endTime;
	private final int intervalTime;
	@CreatedDate
	private final LocalDateTime createdAt;
	@LastModifiedDate
	private final LocalDateTime updatedAt;
	@Embedded.Empty
	private final LineStations stations;

	public Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime,
		LocalDateTime createdAt, LocalDateTime updatedAt, LineStations stations) {
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.endTime = endTime;
		this.intervalTime = intervalTime;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.stations = stations;
	}

	public static Line of(String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
		return new Line(null, name, startTime, endTime, intervalTime, null, null,
			LineStations.empty());
	}

	public Line withId(Long id) {
		return new Line(id, this.name, this.startTime, this.endTime, this.intervalTime,
			this.createdAt, this.updatedAt, this.stations);
	}

	public Line withCreatedAt(LocalDateTime createdAt) {
		return new Line(this.id, this.name, this.startTime, this.endTime, this.intervalTime,
			createdAt, this.updatedAt, this.stations);
	}

	public Line withUpdatedAt(LocalDateTime updatedAt) {
		return new Line(this.id, this.name, this.startTime, this.endTime, this.intervalTime,
			this.createdAt, this.updatedAt, this.stations);
	}

	public Line makeLineUpdateBy(Line line) {
		String updateName = findFirstNotNull(line.name, name);
		LocalTime updateStartTime = findFirstNotNull(line.startTime, startTime);
		LocalTime updateEndTime = findFirstNotNull(line.endTime, endTime);
		int updateIntervalTime = findFirstNotDefault(line.intervalTime, intervalTime);

		return new Line(this.id, updateName, updateStartTime, updateEndTime, updateIntervalTime,
			this.createdAt, this.updatedAt, this.stations);
	}

	private <T> T findFirstNotNull(T priorityFirst, T prioritySecond) {
		if (Objects.nonNull(priorityFirst)) {
			return priorityFirst;
		}
		return prioritySecond;
	}

	private int findFirstNotDefault(int priorityFirst, int prioritySecond) {
		if (priorityFirst != 0) {
			return priorityFirst;
		}
		return prioritySecond;
	}

	public void addLineStation(LineStation lineStation) {
		stations.add(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		stations.removeById(stationId);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public Set<LineStation> getStations() {
		return stations.getStations();
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public List<Long> getStationIds() {
		return stations.getStationIds();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Line line = (Line) o;
		return Objects.equals(id, line.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Line{" +
			"id=" + id +
			", name='" + name + '\'' +
			", startTime=" + startTime +
			", endTime=" + endTime +
			", intervalTime=" + intervalTime +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			", stations=" + stations +
			'}';
	}
}
