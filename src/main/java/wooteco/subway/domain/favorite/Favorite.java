package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;

public class Favorite {
    @Id
    private Long id;
    private Long memberId;
    private Long sourceId;
    private Long targetId;

    public Favorite() {
    }

    private Favorite(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    private Favorite(Long id, Long memberId, Long sourceId, Long targetId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static Favorite of(Long memberId, Long sourceId, Long targetId) {
        return new Favorite(memberId, sourceId, targetId);
    }

    public static Favorite of(Long id, Long memberId, Long sourceId, Long targetId) {
        return new Favorite(id, memberId, sourceId, targetId);
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
}
