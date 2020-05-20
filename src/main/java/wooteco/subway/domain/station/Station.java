package wooteco.subway.domain.station;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Station {
	@Id
	private final Long id;
	private final String name;
	@CreatedDate
	private final LocalDateTime createdAt;

	Station(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;

	}

	public static Station of(String name) {
		return new Station(null, name, null);
	}

	public Station withId(final Long id) {
		return new Station(id, this.name, this.createdAt);
	}

	public Station withCreatedAt(final LocalDateTime createdAt) {
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
}
