package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import wooteco.subway.domain.station.Station;

import java.util.Objects;

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

    public Favorite(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public boolean isSameSourceAndTarget(Station source, Station target) {
        return source.isSameId(sourceId) && target.isSameId(targetId);
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
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
}
