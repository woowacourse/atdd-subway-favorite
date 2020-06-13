package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import wooteco.subway.domain.station.Station;

public class Favorite {
    @Id
    private Long id;
    private Long memberId;
    private Long departureStationId;
    private Long arrivalStationId;

    public Favorite(Long memberId, Long departureStationId, Long arrivalStationId) {
        this(null, memberId, departureStationId, arrivalStationId);
    }

    @PersistenceConstructor
    public Favorite(Long id, Long memberId, Long departureStationId, Long arrivalStationId) {
        this.id = id;
        this.memberId = memberId;
        this.departureStationId = departureStationId;
        this.arrivalStationId = arrivalStationId;
    }

    public static Favorite of(Long memberId, Station departure, Station arrival) {
        return new Favorite(memberId, departure.getId(), arrival.getId());
    }

    public boolean isDuplicate(Favorite favorite) {
        return this.memberId.equals(favorite.memberId) && this.departureStationId.equals(
            favorite.departureStationId) && this.arrivalStationId.equals(favorite.arrivalStationId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public Long getArrivalStationId() {
        return arrivalStationId;
    }
}
