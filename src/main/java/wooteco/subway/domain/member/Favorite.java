package wooteco.subway.domain.member;

import wooteco.subway.domain.station.Station;

import java.util.Objects;

import javax.persistence.*;

/**
 *    즐겨찾기 class입니다.
 *
 *    @author HyungJu An
 */
@Entity
public class Favorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Station sourceStation;
	@OneToOne
	private Station targetStation;
	@ManyToOne
	@JoinColumn
	private Member member;

	protected Favorite() {
	}

	private Favorite(Long id, Station sourceStation, Station targetStation, Member member) {
		this.id = id;
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
		this.member = member;
	}

	public static Favorite of(Station source, Station target) {
		return new Favorite(null, source, target, null);
	}

	public static Favorite of(Long id, Station source, Station target) {
		return new Favorite(id, source, target, null);
	}

	public Long getId() {
		return id;
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

	public void setMember(Member member) {
		if (!member.getFavorites().contains(this)) {
			member.addFavorite(this);
			return;
		}
		this.member = member;
	}
}
