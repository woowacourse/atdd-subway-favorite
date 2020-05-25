package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.acceptance.favorite.dto.StationPathResponse;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.path.StationPath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.exceptions.NotExistStationException;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;

	public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public StationPath registerPath(Member member, String sourceName, String targetName) {
		Station source = stationRepository.findByName(sourceName)
				.orElseThrow(() -> new NotExistStationException(sourceName));
		Station target = stationRepository.findByName(targetName)
				.orElseThrow(() -> new NotExistStationException(targetName));

		StationPath stationPath = StationPath.of(source.getId(), target.getId());
		member.addFavoritePath(stationPath);
		Member savedMember = memberRepository.save(member);
		return savedMember.getRecentlyUpdatedPath();
	}

	public List<StationPathResponse> retrievePath(Member member) {
		List<StationPath> favoritePaths = member.getFavoritePaths();
		return favoritePaths.stream()
				.map(path -> {
					Station source =
							stationRepository.findById(path.getSourceId()).orElseThrow(() -> new NotExistStationException(path.getSourceId()));
					Station target =
							stationRepository.findById(path.getTargetId()).orElseThrow(() -> new NotExistStationException(path.getTargetId()));
					return new StationPathResponse(path.getId(), StationResponse.of(source),
					                               StationResponse.of(target));
				})
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
