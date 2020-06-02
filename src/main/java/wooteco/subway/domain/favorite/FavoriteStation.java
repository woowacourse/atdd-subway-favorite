package wooteco.subway.domain.favorite;

import java.util.Objects;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("FAVORITE")
public class FavoriteStation {

    @Column("member")
    private Long memberId;
    private String source;
    private String target;

    public FavoriteStation() {
    }

    public FavoriteStation(Long memberId, String source, String target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public boolean isSameSourceAndTarget(String source, String target) {
        return this.source.equals(source) && this.target.equals(target);
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FavoriteStation that = (FavoriteStation)o;
        return Objects.equals(memberId, that.memberId) &&
            Objects.equals(source, that.source) &&
            Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, source, target);
    }
}
