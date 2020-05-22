package wooteco.subway.domain.member.favorite;

import org.springframework.data.annotation.PersistenceConstructor;
import wooteco.subway.domain.station.Station;

public class Favorite {
    private Long sourceStationId;
    private Long targetStationId;

    @PersistenceConstructor
    public Favorite(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static Favorite of(final Station source, final Station target) {
        return new Favorite(source.getId(), target.getId());
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
