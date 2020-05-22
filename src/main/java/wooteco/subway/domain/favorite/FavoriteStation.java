package wooteco.subway.domain.favorite;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("FAVORITE")
public class FavoriteStation {
    @Id
    private Long id;
    private Long memberId;
    private Long source;
    private Long target;

    public FavoriteStation() {
    }

    public FavoriteStation(Long id, Long memberId, Long source, Long target) {
        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
