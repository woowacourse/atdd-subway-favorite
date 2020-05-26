package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    private Long id;
    private Long sourceId;
    private Long targetId;

    public Favorite() {
    }

    public Favorite(Long id, Long sourceId, Long targetId) {
        this.id = id;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static Favorite of(Long sourceId, Long targetId) {
        return new Favorite(null, sourceId, targetId);
    }

    public Long getId() {
        return id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Favorite favorite = (Favorite)o;
        return Objects.equals(sourceId, favorite.sourceId) &&
            Objects.equals(targetId, favorite.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, targetId);
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }
}
