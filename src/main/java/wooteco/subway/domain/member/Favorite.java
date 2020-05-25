package wooteco.subway.domain.member;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    private Long id;
    private Long departureId;
    private Long destinationId;

    private Favorite() {
    }

    public Favorite(Long id, Long departureId, Long destinationId) {
        this.id = id;
        this.departureId = departureId;
        this.destinationId = destinationId;
    }

    public static Favorite of(Long departureId, Long destinationId) {
        return new Favorite(null, departureId, destinationId);
    }

    public Long getId() {
        return id;
    }

    public Long getDepartureId() {
        return departureId;
    }

    public Long getDestinationId() {
        return destinationId;
    }
}
