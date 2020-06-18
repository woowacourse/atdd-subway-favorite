package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.annotation.Id;

import wooteco.subway.domain.BaseEntity;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;

public class Favorite extends BaseEntity {
	@Id
	private Long id;
	private Long memberId;
	private Long sourceStationId;
	private Long targetStationId;

	public Favorite() {
	}

	public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
		this.memberId = memberId;
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public Favorite(Long id, Long memberId, Long sourceStationId, Long targetStationId) {
		this.id = id;
		this.memberId = memberId;
		this.sourceStationId = sourceStationId;
		this.targetStationId = targetStationId;
	}

	public boolean isNotSameMember(Member member) {
		return !Objects.equals(member.getId(), memberId);
	}

	public Long getId() {
		return id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public Long getSourceStationId() {
		return sourceStationId;
	}

	public Long getTargetStationId() {
		return targetStationId;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		final Favorite favorite = (Favorite)o;
		return Objects.equals(sourceStationId, favorite.sourceStationId) &&
			Objects.equals(targetStationId, favorite.targetStationId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceStationId, targetStationId);
	}

	public boolean isSameValue(FavoriteRequest favoriteRequest) {
		return Objects.equals(sourceStationId, favoriteRequest.getSourceStationId())
			&& Objects.equals(targetStationId, favoriteRequest.getTargetStationId());
	}
}
