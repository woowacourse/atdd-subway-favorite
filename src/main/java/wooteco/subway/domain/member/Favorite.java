package wooteco.subway.domain.member;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(final Long id, final Long sourceStationId, final Long targetStationId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(final Long sourceStationId, final Long targetStationId) {
        this(null, sourceStationId, targetStationId);
    }

    public boolean isId(Long id) {
        return this.id.equals(id);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Favorite favorite = (Favorite)o;
        return Objects.equals(id, favorite.id) &&
            Objects.equals(sourceStationId, favorite.sourceStationId) &&
            Objects.equals(targetStationId, favorite.targetStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceStationId, targetStationId);
    }
}
