package wooteco.subway.service.member.vo;

import wooteco.subway.domain.station.Station;

public class FavoriteInfo {
	private final Station sourceStation;
	private final Station targetStation;

	public FavoriteInfo(final Station sourceStation, final Station targetStation) {
		this.sourceStation = sourceStation;
		this.targetStation = targetStation;
	}

	public Station getSourceStation() {
		return sourceStation;
	}

	public Station getTargetStation() {
		return targetStation;
	}
}
