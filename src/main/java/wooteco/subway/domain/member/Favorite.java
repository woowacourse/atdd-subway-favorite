package wooteco.subway.domain.member;

import java.util.Objects;

public class Favorite {
	private final Long sourceId;
	private final Long targetId;

	public Favorite(Long sourceId, Long targetId) {
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public static Favorite of(Long sourceId, Long targetId) {
		return new Favorite(sourceId, targetId);
	}

	public boolean hasSourceId(Long sourceId) {
		return this.sourceId.equals(sourceId);
	}

	public boolean hasTargetId(Long targetId) {
		return this.targetId.equals(targetId);
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Favorite favorite = (Favorite) o;
		return Objects.equals(sourceId, favorite.sourceId) &&
				Objects.equals(targetId, favorite.targetId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceId, targetId);
	}
}
