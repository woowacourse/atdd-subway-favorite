package wooteco.subway.domain.path;

import org.springframework.data.annotation.Id;

public class StationPath {
	@Id
	private Long id;
	private Long sourceId;
	private Long targetId;

	public StationPath(Long id, Long sourceId, Long targetId) {
		this.id = id;
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public static StationPath of(Long source, Long target) {
		return new StationPath(null, source, target);
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
}
