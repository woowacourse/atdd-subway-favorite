package wooteco.subway.domain.favorite;

import java.util.Objects;

public class Favorite {
	private final long source;
	private final long target;

	public Favorite(long source, long target) {
		this.source = source;
		this.target = target;
	}

	public long getSource() {
		return source;
	}

	public long getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Favorite favorite = (Favorite)o;
		return Objects.equals(source, favorite.source) &&
			Objects.equals(target, favorite.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target);
	}
}
