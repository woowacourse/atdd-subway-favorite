package wooteco.subway.domain.member;

import java.util.Objects;

import javax.validation.constraints.NotNull;

/**
 *    즐겨찾기 class입니다.
 *
 *    @author HyungJu An
 */
public class Favorite {
	@NotNull
	private final Long sourceId;
	@NotNull
	private final Long targetId;

	Favorite(final Long sourceId, final Long targetId) {
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public static Favorite of(final Long source, final Long target) {
		return new Favorite(source, target);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Favorite favorite = (Favorite)o;
		return sourceId.equals(favorite.sourceId) &&
			targetId.equals(favorite.targetId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceId, targetId);
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}
}
