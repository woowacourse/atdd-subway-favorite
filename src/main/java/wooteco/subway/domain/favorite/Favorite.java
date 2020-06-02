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

    public Favorite(Long id, Long memberId, Long sourceId, Long targetId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Favorite(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
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

    public boolean isNotEqualToMemberId(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    @Override
    public String toString() {
        return "Favorite{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", sourceId=" + sourceId +
                ", targetId=" + targetId +
                '}';
    }
}
