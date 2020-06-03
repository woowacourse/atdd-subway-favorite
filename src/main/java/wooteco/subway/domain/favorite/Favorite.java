package wooteco.subway.domain.favorite;

import java.util.Objects;
import org.springframework.data.annotation.Id;

public class Favorite {

	@Id
	private Long id;
	private final Long sourceStationId;
	private final Long targetStationId;

	public Favorite(long sourceStationId, long targetStationId) {
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public long getSourceStationId() {
		return sourceStationId;
	}

	public long getTargetStationId() {
		return targetStationId;
	}

	public boolean equalPath(Favorite favorite) {
		return this.sourceStationId.equals(favorite.sourceStationId)
			&& this.targetStationId.equals(favorite.targetStationId);
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
		return Objects.equals(id, favorite.id) &&
			Objects.equals(sourceStationId, favorite.sourceStationId) &&
			Objects.equals(targetStationId, favorite.targetStationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, sourceStationId, targetStationId);
	}
}

