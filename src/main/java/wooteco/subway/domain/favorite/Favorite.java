package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Favorite {
	@Id
	private final Long id;
	private final long source;
	private final long target;

	Favorite(Long id, long source, long target) {
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public static Favorite of(long source, long target) {
		return new Favorite(null, source, target);
	}

	public Favorite withId(Long id) {
		return new Favorite(id, this.source, this.target);
	}

	public boolean equalsSourceAndTarget(long source, long target) {
		return this.source == source && this.target == target;
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
		return source == favorite.source &&
			target == favorite.target &&
			Objects.equals(id, favorite.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target);
	}

	@Override
	public String toString() {
		return "Favorite{" +
			"id=" + id +
			", source=" + source +
			", target=" + target +
			'}';
	}
}
