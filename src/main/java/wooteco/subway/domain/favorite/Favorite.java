package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    private final Long id;
    private final Long sourceStationId;
    private final Long targetStationId;

    public Favorite(Long id, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(Long sourceStationId, Long targetStationId) {
        this(null, sourceStationId, targetStationId);
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
}
