package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.Objects;

public class Favorite {
    @Id
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    public Favorite() {
    }

    public Favorite(Long id, Long memberId, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Favorite(Long memberId, Long sourceStationId, Long targetStationId) {
        this(null, memberId, sourceStationId, targetStationId);
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

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) &&
                Objects.equals(sourceStationId, favorite.sourceStationId) &&
                Objects.equals(targetStationId, favorite.targetStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceStationId, targetStationId);
    }
}
