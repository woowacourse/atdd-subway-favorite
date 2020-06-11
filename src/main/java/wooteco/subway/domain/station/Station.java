package wooteco.subway.domain.station;

import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Station {

	@Id
	private final Long id;
	private final String name;
	@CreatedDate
	private final LocalDateTime createdAt;

	private Station(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public static Station of(String name) {
		return new Station(null, name, null);
	}

	public Station withId(Long id) {
		return new Station(id, this.name, this.createdAt);
	}

	public Station withCreatedAt(LocalDateTime createdAt) {
		return new Station(this.id, this.name, createdAt);
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
