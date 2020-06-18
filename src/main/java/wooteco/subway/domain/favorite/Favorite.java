package wooteco.subway.domain.favorite;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import wooteco.subway.service.favorite.dto.FavoriteRequest;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;
    // @Column(name="member")
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
}
