package wooteco.subway.domain.favorite;

import static javax.persistence.FetchType.*;

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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@AttributeOverride(name = "id", column = @Column(name = "FAVORITE_ID"))
public class Favorite extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SOURCE_STATION_ID")
    private Station sourceStation;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TARGET_STATION_ID")
    private Station targetStation;

    public Favorite(Long id, Member member, Station source, Station target) {
        this(member, source, target);
        super.id = id;
    }

    public static Favorite of(Member member, Station sourceStation, Station targetStation) {
        return new Favorite(member, sourceStation, targetStation);
    }

    public void validateMember(Member member) {
        if (!this.member.equals(member)) {
            throw new AuthenticationException();
        }
    }
}
