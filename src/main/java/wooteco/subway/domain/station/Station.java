package wooteco.subway.domain.station;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Station {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String name;
	@CreatedDate
	private LocalDateTime createdAt;

	protected Station() {
	}

	private Station(Long id, String name, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}

	public static Station of(Long id, String name) {
		return new Station(id, name, null);
	}

	public static Station of(String name) {
		return new Station(null, name, null);
	}

	public boolean isSameId(Long id) {
		return Objects.equals(this.id, id);
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
