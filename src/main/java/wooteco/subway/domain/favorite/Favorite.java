package wooteco.subway.domain.favorite;

public class Favorite {
    private final Long sourceStationId;
    private final Long targetStationId;

    public Favorite(long sourceStationId, long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }
}

