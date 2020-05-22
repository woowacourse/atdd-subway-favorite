package wooteco.subway.domain.member.favorite;

import org.springframework.data.annotation.PersistenceConstructor;

public class Favorite {
    private Long sourceStationId;
    private Long targetStationId;

    @PersistenceConstructor
    public Favorite(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
