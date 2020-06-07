package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("FAVORITE")
public class FavoriteStation {
    @Id
    private Long id;
    @Column("member")
    private Long memberId;
    @Column("source_id")
    private Long sourceId;
    @Column("target_id")
    private Long targetId;

    public FavoriteStation() {
    }

    public FavoriteStation(Long memberId, Long sourceId, Long targetId) {
        this.memberId = memberId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public FavoriteStation(Long id, Long memberId, Long sourceId, Long targetId) {
        this.id = id;
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

    public boolean isSameSourceId(Long sourceId) {
        return Objects.equals(this.sourceId, sourceId);
    }

    public boolean isSameTargetId(Long targetId) {
        return Objects.equals(this.targetId, targetId);
    }
}
