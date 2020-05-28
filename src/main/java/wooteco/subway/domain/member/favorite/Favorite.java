package wooteco.subway.domain.member.favorite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import wooteco.subway.domain.station.Station;

public class Favorite {
    private Long sourceStationId;
    private Long targetStationId;

    private Favorite() {
    }

    public Favorite(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static Favorite of(final Station source, final Station target) {
        return new Favorite(source.getId(), target.getId());
    }

    public List<Long> getStationIds() {
        return Arrays.asList(sourceStationId, targetStationId);
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Favorite favorite = (Favorite)o;
        return Objects.equals(sourceStationId, favorite.sourceStationId) &&
            Objects.equals(targetStationId, favorite.targetStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceStationId, targetStationId);
    }
}
