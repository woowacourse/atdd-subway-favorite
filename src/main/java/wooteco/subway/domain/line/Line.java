package wooteco.subway.domain.line;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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

	Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime,
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
		return new Line(null, name, startTime, endTime, intervalTime, null, null, LineStations.empty());
	}

	public Line withId(Long id) {
		return new Line(id, this.name, this.startTime, this.endTime, this.intervalTime, this.createdAt, this.updatedAt, this.stations);
	}

	public Line withCreatedAt(LocalDateTime createdAt) {
		return new Line(this.id, this.name, this.startTime, this.endTime, this.intervalTime, createdAt, this.updatedAt, this.stations);
	}

	public Line withUpdatedAt(LocalDateTime updatedAt) {
		return new Line(this.id, this.name, this.startTime, this.endTime, this.intervalTime, this.createdAt, updatedAt, this.stations);
	}

	public Line update(Line line) {
		return new Line(this.id, line.name, line.startTime, line.endTime, line.intervalTime, this.createdAt,
			this.updatedAt, this.stations);
	}

	public void addLineStation(LineStation lineStation) {
		stations.add(lineStation);
	}

	public void removeLineStationById(Long stationId) {
		stations.removeById(stationId);
	}

	public List<Long> getStationIds() {
		return stations.getStationIds();
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
}
