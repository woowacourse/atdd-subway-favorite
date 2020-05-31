package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.exceptions.NotExistStationException;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FavoriteService {
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;

	public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public FavoritePath registerPath(Member member, String sourceName, String targetName) {
		Station source = stationRepository.findByName(sourceName)
				.orElseThrow(() -> new NotExistStationException(sourceName));
		Station target = stationRepository.findByName(targetName)
				.orElseThrow(() -> new NotExistStationException(targetName));

		FavoritePath favoritePath = FavoritePath.of(source.getId(), target.getId());
		member.addFavoritePath(favoritePath);
		Member savedMember = memberRepository.save(member);
		return savedMember.getRecentlyUpdatedPath();
	}

	public List<FavoritePathResponse> retrievePath(Member member) {
		List<Long> favoritePathsIds = member.getFavoritePathsIds();
		List<Long> favoritePathsStationsIds = member.getFavoritePathsStationsIds();
		List<Station> favoritePathsStations = stationRepository.findAllById(favoritePathsStationsIds);

		return IntStream.range(0, favoritePathsIds.size())
				.mapToObj(idx -> new FavoritePathResponse(favoritePathsIds.get(idx),
				                                          StationResponse.of(favoritePathsStations.get(idx * 2)),
				                                          StationResponse.of(favoritePathsStations.get(idx * 2 + 1))))
				.collect(Collectors.toList());
	}

	public void deletePath(Member member, long pathId) {
		if (member.hasNotPath(pathId)) {
			throw new NotExistFavoritePathException(pathId);
		}
		member.deletePath(pathId);
		memberRepository.save(member);
	}
}
