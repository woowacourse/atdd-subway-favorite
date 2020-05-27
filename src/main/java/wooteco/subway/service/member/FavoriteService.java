package wooteco.subway.service.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
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

	public void createFavorite(final Member member, final FavoriteRequest favoriteRequest) {
		stationRepository.findById(favoriteRequest.getSourceId())
			.orElseThrow(() -> new NotFoundStationException("출발역을 찾을 수 없습니다. : " + favoriteRequest.getSourceId()));
		stationRepository.findById(favoriteRequest.getTargetId())
			.orElseThrow(() -> new NotFoundStationException("도착역을 찾을 수 없습니다. : " + favoriteRequest.getTargetId()));
		memberRepository.save(member.addFavorite(favoriteRequest.toFavorite()));
	}

	public List<Station> retrieveStationsBy(final Set<Favorite> favorites) {
		List<Station> stations = new ArrayList<>();
		for (Favorite favorite : favorites) {
			stations.add(stationRepository.findById(favorite.getSourceId())
				.orElseThrow(() -> new NotFoundStationException("출발역을 찾을 수 없습니다. : " + favorite.getSourceId())));
			stations.add(stationRepository.findById(favorite.getTargetId())
				.orElseThrow(() -> new NotFoundStationException("도착역을 찾을 수 없습니다. : " + favorite.getTargetId())));
		}
		return stations;
	}
}
