package wooteco.subway.service.member;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.vo.FavoriteInfo;
import wooteco.subway.service.station.NotFoundStationException;

/**
 *    즐겨찾기 Service 클래스입니다.
 *
 *    @author HyungJu An
 */
@Service
public class FavoriteService {
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;

	public FavoriteService(final MemberRepository memberRepository,
		final StationRepository stationRepository) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public void createFavorite(final Member member, final Favorite favorite) {
		findSourceStation(favorite);
		findTargetStation(favorite);
		memberRepository.save(member.addFavorite(favorite));
	}

	private Station findTargetStation(final Favorite favorite) {
		return stationRepository.findById(favorite.getTargetId())
			.orElseThrow(() -> new NotFoundStationException("도착역을 찾을 수 없습니다. : " + favorite.getTargetId()));
	}

	private Station findSourceStation(final Favorite favorite) {
		return stationRepository.findById(favorite.getSourceId())
			.orElseThrow(() -> new NotFoundStationException("출발역을 찾을 수 없습니다. : " + favorite.getSourceId()));
	}

	public List<FavoriteInfo> getFavoriteInfos(final Member member) {
		return member.getFavorites()
			.stream()
			.map(it -> new FavoriteInfo(findSourceStation(it), findTargetStation(it)))
			.collect(Collectors.toList());
	}

	public void deleteFavorite(final Member member, final Favorite favorite) {
		memberRepository.save(member.deleteFavorite(favorite));
	}
}
