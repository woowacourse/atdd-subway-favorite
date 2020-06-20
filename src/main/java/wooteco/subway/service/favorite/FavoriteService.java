package wooteco.subway.service.favorite;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteNotFoundException;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final StationRepository stationRepository;

	public FavoriteService(FavoriteRepository favoriteRepository,
		StationRepository stationRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
	}

	public Long add(Member member, FavoriteRequest favoriteRequest) {
		Optional<Favorite> duplicateFavorite = findDuplicateFavorite(member, favoriteRequest);

		if (duplicateFavorite.isPresent()) {
			return duplicateFavorite.get().getId();
		}

		Favorite favorite = favoriteRequest.toEntity(member.getId());
		Favorite persistenceFavorite = favoriteRepository.save(favorite);

		return persistenceFavorite.getId();
	}

	public void delete(Member member, Long favoriteId) {
		Favorite persistenceFavorite = favoriteRepository.findById(favoriteId)
			.orElseThrow(FavoriteNotFoundException::new);

		if (persistenceFavorite.isNotSameMember(member)) {
			throw new IllegalArgumentException("잘못된 삭제 요청입니다." + member + "FavoriteId:" + favoriteId);
		}

		favoriteRepository.delete(persistenceFavorite);
	}

	public List<FavoriteResponse> findFavorites(Member member) {
		Map<Long, Station> stationsById = stationRepository.findAll().stream()
			.collect(Collectors.toMap(Station::getId, station -> station));
		List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());

		return favorites.stream()
			.map(favorite -> new FavoriteResponse(
				stationsById.get(favorite.getSourceStationId()).getName(),
				stationsById.get(favorite.getTargetStationId()).getName()
			)).collect(Collectors.toList());
	}

	private Optional<Favorite> findDuplicateFavorite(Member member, FavoriteRequest favoriteRequest) {
		List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());

		return favorites.stream()
			.filter(favoriteIter -> favoriteIter.isSameValue(favoriteRequest))
			.findAny();
	}
}
