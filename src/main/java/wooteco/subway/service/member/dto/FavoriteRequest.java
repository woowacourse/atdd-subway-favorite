package wooteco.subway.service.member.dto;

import javax.validation.constraints.NotNull;

import wooteco.subway.domain.member.Favorite;

/**
 *    즐겨찾기 요청 DTO 클래스입니다.
 *
 *    @author HyungJu An
 */
public class FavoriteRequest {
	@NotNull
	private Long sourceId;
	@NotNull
	private Long targetId;

	private FavoriteRequest() {
	}

	public FavoriteRequest(Long sourceId, Long targetId) {
		this.sourceId = sourceId;
		this.targetId = targetId;
	}

	public Favorite toFavorite() {
		return Favorite.of(sourceId, targetId);
	}

	public Long getSourceId() {
		return sourceId;
	}

	public Long getTargetId() {
		return targetId;
	}
}
