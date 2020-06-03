package wooteco.subway.domain.favorite;

import java.util.Objects;

import wooteco.subway.exception.SameSourceTargetStationException;

public class Favorite {
    private final Long sourceStationId;
    private final Long targetStationId;

    public Favorite(Long sourceStationId, Long targetStationId) {
        validateStationId(sourceStationId, targetStationId);
        this.sourceStationId = Objects.requireNonNull(sourceStationId);
        this.targetStationId = Objects.requireNonNull(targetStationId);
    }

    private void validateStationId(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new SameSourceTargetStationException();
        }
    }

    public long getSourceStationId() {
        return sourceStationId;
    }

    public long getTargetStationId() {
        return targetStationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Favorite favorite = (Favorite)o;
        return Objects.equals(sourceStationId, favorite.sourceStationId) &&
            Objects.equals(targetStationId, favorite.targetStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceStationId, targetStationId);
    }
}

