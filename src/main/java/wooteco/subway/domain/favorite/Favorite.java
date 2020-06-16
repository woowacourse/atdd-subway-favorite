package wooteco.subway.domain.favorite;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.domain.common.BaseEntity;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.web.exception.AuthenticationException;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@AttributeOverride(name = "id", column = @Column(name = "FAVORITE_ID"))
public class Favorite extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "SOURCE_STATION_ID")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "TARGET_STATION_ID")
    private Station targetStation;

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        return new Favorite(member, sourceStation, targetStation);
    }

    public void validateMember(Member member) {
        if (!this.member.isSameId(member)) {
            throw new AuthenticationException();
        }
    }

    public void applyFavorite(Member member) {
        this.member = member;
    }
}
