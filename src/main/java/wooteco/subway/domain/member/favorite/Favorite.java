package wooteco.subway.domain.member.favorite;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @ManyToOne
    private Station sourceStation;
    @ManyToOne
    private Station targetStation;

    protected Favorite() {
    }

    public Favorite(final Station sourceStation, final Station targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Favorite(final Member member, final Station sourceStation, final Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Favorite(final Long id, final Member member, final Station sourceStation,
        final Station targetStation) {
        this.id = id;
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public List<Long> getStationIds() {
        return Arrays.asList(sourceStation.getId(), targetStation.getId());
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public Member getMember() {
        return member;
    }

    public Long getId() {
        return id;
    }

    public void setMember(Member member) {
        member.addFavorite(this);
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Favorite favorite = (Favorite)o;
        return Objects.equals(id, favorite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
