package wooteco.subway.domain.line;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import wooteco.subway.domain.station.Station;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	private LocalTime startTime;
	private LocalTime endTime;
	private int intervalTime;
	@CreatedDate
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime updatedAt;
	private LineStations stations = LineStations.empty();

	protected Line() {
	}

	private Line(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime,
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

	public static Line of(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
		return new Line(id, name, startTime, endTime, intervalTime, null, null, LineStations.empty());
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
		stations.removeById(stationId);
	}

	public List<Station> getSortedStations() {
		return stations.getSortedStations();
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
