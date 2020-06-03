package wooteco.subway.domain.path;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import wooteco.subway.jdbc.BaseEntity;

public class FavoritePath extends BaseEntity {
    @Id
    private Long id;
    private Long sourceId;
    private Long targetId;
    private Long memberId;

    @PersistenceConstructor
    public FavoritePath(Long id, Long sourceId, Long targetId, Long memberId) {
        super(null, null);
        this.id = id;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.memberId = memberId;
    }

    public FavoritePath(Long sourceId, Long targetId, Long memberId) {
        this(null, sourceId, targetId, memberId);
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

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FavoritePath that = (FavoritePath)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
