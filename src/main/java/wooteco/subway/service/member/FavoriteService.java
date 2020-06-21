package wooteco.subway.service.member;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.station.NotFoundStationException;

/**
 *    즐겨찾기 Service 클래스입니다.
 *
 *    @author HyungJu An
 */
@Service
public class FavoriteService {
	private final StationRepository stationRepository;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(final StationRepository stationRepository, final FavoriteRepository favoriteRepository) {
		this.stationRepository = stationRepository;
		this.favoriteRepository = favoriteRepository;
	}

	public void createFavorite(final Member member, final FavoriteRequest favoriteRequest) {
		Favorite persist = favoriteRepository.save(Favorite.of(findSourceStation(favoriteRequest.getSourceId()), findTargetStation(favoriteRequest.getTargetId())));
		member.addFavorite(persist);
	}

	private Station findSourceStation(final Long id) {
		return stationRepository.findById(id)
				.orElseThrow(() -> new NotFoundStationException("출발역을 찾을 수 없습니다. : " + id));
	}

	private Station findTargetStation(final Long id) {
		return stationRepository.findById(id)
			.orElseThrow(() -> new NotFoundStationException("도착역을 찾을 수 없습니다. : " + id));
	}

	public List<FavoriteResponse> getFavorites(final Member member) {
		return FavoriteResponse.listOf(member.getFavorites());
	}

	public void deleteFavorite(final Member member, final Long id) {
		member.deleteFavoriteById(id);
	}
}
