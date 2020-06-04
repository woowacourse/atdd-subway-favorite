package wooteco.subway.domain.station;

import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Station {

	@Id
	private Long id;
	private String name;
	@CreatedDate
	private LocalDateTime createdAt;

	public Station() {
	}

	public Station(String name) {
		this.name = name;
	}

	public Station(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Station station = (Station) o;
		if (Objects.isNull(this.id) || Objects.isNull(station.id)) {
			return Objects.equals(name, station.name) &&
				Objects.equals(createdAt, station.createdAt);
		}
		return id.equals(station.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Station{" +
			"id=" + id +
			", name='" + name + '\'' +
			", createdAt=" + createdAt +
			'}';
	}
}
