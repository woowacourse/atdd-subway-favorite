package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import wooteco.subway.service.favorite.dto.FavoriteRequest;

public class Favorite {
    @Id
    private Long id;
    private Long memberId;
    private String departure;
    private String arrival;

    public Favorite(Long memberId, String departure, String arrival) {
        this(null, memberId, departure, arrival);
    }

    public Favorite(Long id, Long memberId, String departure, String arrival) {
        this.id = id;
        this.memberId = memberId;
        this.departure = departure;
        this.arrival = arrival;
    }

    public static Favorite of(Long memberId, FavoriteRequest favoriteRequest) {
        return new Favorite(memberId, favoriteRequest.getDeparture(), favoriteRequest.getArrival());
    }

    public boolean isDuplicate(Favorite favorite) {
        return this.memberId.equals(favorite.memberId) && this.departure.equals(favorite.departure) && this.arrival.equals(favorite.arrival);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }
}
