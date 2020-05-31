package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import wooteco.subway.service.favorite.dto.FavoriteRequest;

public class Favorite {
    @Id
    private Long id;
    private Long memberId;
    private Long departureId;
    private Long arrivalId;

    public Favorite(Long memberId, Long departureId, Long arrivalId) {
        this(null, memberId, departureId, arrivalId);
    }

    @PersistenceConstructor
    public Favorite(Long id, Long memberId, Long departureId, Long arrivalId) {
        this.id = id;
        this.memberId = memberId;
        this.departureId = departureId;
        this.arrivalId = arrivalId;
    }

    public static Favorite of(Long memberId, FavoriteRequest favoriteRequest) {
        return new Favorite(memberId, favoriteRequest.getDepartureId(), favoriteRequest.getArrivalId());
    }

    public boolean isDuplicate(Favorite favorite) {
        return this.memberId.equals(favorite.memberId)
                && this.departureId.equals(favorite.departureId)
                && this.arrivalId.equals(favorite.arrivalId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getArrivalId() {
        return arrivalId;
    }
}
