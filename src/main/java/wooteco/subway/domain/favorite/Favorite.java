package wooteco.subway.domain.favorite;

import java.util.Objects;
import org.springframework.data.annotation.Id;

public class Favorite {

	@Id
	private final Long id;
	private final Long sourceStationId;
	private final Long targetStationId;

	private Favorite(Long id, Long sourceStationId, Long targetStationId) {
		this.id = id;
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public static Favorite of(long sourceStationId, long targetStationId) {
		return new Favorite(null, sourceStationId, targetStationId);
	}

	public Favorite withId(Long id) {
		return new Favorite(id, this.sourceStationId, this.targetStationId);
	}

	public boolean equalPath(Favorite favorite) {
		return this.sourceStationId.equals(favorite.sourceStationId)
			&& this.targetStationId.equals(favorite.targetStationId);
	}

	public long getSourceStationId() {
		return sourceStationId;
	}

	public long getTargetStationId() {
		return targetStationId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Favorite favorite = (Favorite) o;
		return Objects.equals(id, favorite.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}

