package wooteco.subway.service.favorite;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.exception.StationNotFoundException;
import wooteco.subway.service.favorite.exception.TargetEqualsSourceException;

@Service
public class FavoriteService {
	private MemberRepository memberRepository;
	private StationRepository stationRepository;

	public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public Favorite createFavorite(Member member, FavoriteRequest favoriteRequest) {
		validateStationsId(favoriteRequest);

		Favorite favorite = new Favorite(favoriteRequest.getPreStationId(),
			favoriteRequest.getStationId());
		member.addFavorite(favorite);
		Member updatedMember = memberRepository.save(member);
		return updatedMember.findEqualFavoriteTo(favorite);
	}

	private void validateStationsId(FavoriteRequest favoriteRequest) {
		Long preStationId = favoriteRequest.getPreStationId();
		Long stationId = favoriteRequest.getStationId();

		if (preStationId.equals(stationId)) {
			throw new TargetEqualsSourceException();
		}

		stationRepository.findById(preStationId).orElseThrow(StationNotFoundException::new);
		stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
	}

	public List<FavoriteResponse> getFavorites(Member member) {
		List<Long> stationIds = member.getStationIdsFromFavorites();
		List<Station> stations = stationRepository.findAllById(stationIds);
		List<Favorite> favorites = member.getFavorites();

		return favorites.stream()
			.map(it -> FavoriteResponse.of(it.getId(), mapStationById(stations, it.getPreStation()),
				mapStationById(stations, it.getStation())))
			.collect(Collectors.toList());
	}

	private Station mapStationById(List<Station> stations, Long stationId) {
		return stations.stream()
			.filter(station -> station.getId().equals(stationId))
			.findFirst()
			.orElseThrow(StationNotFoundException::new);
	}

	public void deleteFavorite(Member member, Long favoriteId) {
		try {
			member.removeFavoriteById(favoriteId);
			memberRepository.save(member);
		} catch (RuntimeException e) {
			System.out.println(e);
		}
	}
}
