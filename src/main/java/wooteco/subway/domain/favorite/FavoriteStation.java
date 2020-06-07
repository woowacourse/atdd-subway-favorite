package wooteco.subway.domain.favorite;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Table("FAVORITE")
public class FavoriteStation {

    @Column("member")
    private Long memberId;
    private Long source;
    private Long target;

    public FavoriteStation() {
    }

    public FavoriteStation(Long memberId, Long source, Long target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteStation that = (FavoriteStation) o;
        return Objects.equals(memberId, that.memberId) &&
                Objects.equals(source, that.source) &&
                Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, source, target);
    }
}
