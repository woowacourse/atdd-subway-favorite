package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import wooteco.subway.service.favorite.dto.FavoriteRequest;

public class Favorite {
    @Id
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;
    @Column("member")
    private Long memberId;

    public Favorite() {
    }

    public Favorite(final Long id, final Long sourceStationId, final Long targetStationId,
        final Long memberId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.memberId = memberId;
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static Favorite of(FavoriteRequest favoriteRequest) {
        return new Favorite(favoriteRequest.getSourceStationId(),
            favoriteRequest.getTargetStationId());
    }

    public Long getId() {
        return id;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getMemberId() {
        return memberId;
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
}
