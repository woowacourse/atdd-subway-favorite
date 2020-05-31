package wooteco.subway.domain.path;

import org.springframework.data.annotation.Id;

import java.util.Arrays;
import java.util.List;

public class FavoritePath {
	@Id
	private Long id;
	private Long sourceId;
	private Long targetId;

	public FavoritePath(Long id, Long sourceId, Long targetId) {
		this.id = id;
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public static FavoritePath of(Long source, Long target) {
		return new FavoritePath(null, source, target);
	}

	public Long getId() {
		return id;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public List<Long> getStationsId() {
		return Arrays.asList(sourceId, targetId);
	}
}
