package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.station.StationService;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FavoriteService {
	private final MemberRepository memberRepository;
	private final StationService stationService;

	public FavoriteService(MemberRepository memberRepository, StationService stationService) {
		this.memberRepository = memberRepository;
		this.stationService = stationService;
	}

	@Transactional
	public Long registerPath(Member member, String sourceName, String targetName) {
		Station source = stationService.findStationByName(sourceName);
		Station target = stationService.findStationByName(targetName);

		FavoritePath favoritePath = FavoritePath.of(source.getId(), target.getId());
		member.addFavoritePath(favoritePath);
		Member savedMember = memberRepository.save(member);
		return savedMember.getRecentlyUpdatedPath().getId();
	}

	public List<FavoritePathResponse> retrievePath(Member member) {
		List<Long> favoritePathsIds = member.getFavoritePathsIds();
		List<Long> favoritePathsStationsIds = member.getFavoritePathsStationsIds();
		Stations favoritePathsStations = stationService.findStationsByIds(favoritePathsStationsIds);

		return IntStream.range(0, favoritePathsIds.size())
				.mapToObj(idx -> new FavoritePathResponse(favoritePathsIds.get(idx),
				                                          StationResponse.of(favoritePathsStations.getSourceStation(idx)),
				                                          StationResponse.of(favoritePathsStations.getTargetStation(idx))))
				.collect(Collectors.toList());
	}

	@Transactional
	public void deletePath(Member member, long pathId) {
		if (member.hasNotPath(pathId)) {
			throw new NotExistFavoritePathException(pathId);
		}
		member.deletePath(pathId);
		memberRepository.save(member);
	}
}
