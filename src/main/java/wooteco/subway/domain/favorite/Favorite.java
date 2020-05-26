package wooteco.subway.domain.favorite;

import java.util.Objects;

public class Favorite {
	private Long source;
	private Long target;

	public Favorite(Long source, Long target) {
		this.source = source;
		this.target = target;
	}

	public Long getSource() {
		return source;
	}

	public Long getTarget() {
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
