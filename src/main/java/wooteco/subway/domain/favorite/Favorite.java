package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Favorite {
	@Id
	private final Long id;
	private final long sourceId;
	private final long targetId;

	Favorite(Long id, long sourceId, long targetId) {
		this.id = id;
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public static Favorite of(long sourceId, long targetId) {
		return new Favorite(null, sourceId, targetId);
	}

	public Favorite withId(Long id) {
		return new Favorite(id, this.sourceId, this.targetId);
	}

	public boolean equalsSourceAndTarget(long sourceId, long targetId) {
		return this.sourceId == sourceId && this.targetId == targetId;
	}

	public Long getId() {
		return id;
	}

	public long getSourceId() {
		return sourceId;
	}

	public long getTargetId() {
		return targetId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Favorite favorite = (Favorite)o;
		return sourceId == favorite.sourceId &&
			targetId == favorite.targetId &&
			Objects.equals(id, favorite.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, sourceId, targetId);
	}

	@Override
	public String toString() {
		return "Favorite{" +
			"id=" + id +
			", source=" + sourceId +
			", target=" + targetId +
			'}';
	}
}
