package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    private Long id;
    private Long memberId;
    private Long sourceId;
    private Long targetId;

    protected Favorite() {
    }

    public Favorite(Long id, Long memberId, Long sourceId, Long targetId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static Favorite of(Long id, Long memberId, Long sourceId, Long targetId) {
        return new Favorite(id, memberId, sourceId, targetId);
    }

    public static Favorite of(Long memberId, Long sourceId, Long targetId) {
        return new Favorite(null, memberId, sourceId, targetId);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }
}
