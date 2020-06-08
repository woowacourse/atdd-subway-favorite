package wooteco.subway.service.favorite;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationIdNameMap;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
	private MemberRepository memberRepository;
	private StationRepository stationRepository;

	public FavoriteService(MemberRepository memberRepository,
		StationRepository stationRepository) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public void create(Member member, FavoriteCreateRequest favoriteCreateRequest) {
		Long source = findStationIdByName(favoriteCreateRequest.getSource());
		Long target = findStationIdByName(favoriteCreateRequest.getTarget());
		Favorite favorite = Favorite.of(source, target);

		Member persistMember = findMemberById(member)
			.addFavorite(favorite);

		memberRepository.save(persistMember);
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> find(Member member) {
		Member persistMember = findMemberById(member);

		StationIdNameMap stationIdNameMap = mapStationIdToName(
			persistMember.getAllStationIds());

		Favorites favorites = persistMember.getFavorites();
		List<String> orderedSourceNames = stationIdNameMap.getSourceNames(favorites);
		List<String> orderedTargetNames = stationIdNameMap.getTargetNames(favorites);

		return FavoriteResponse.listOf(orderedSourceNames, orderedTargetNames);
	}

	private StationIdNameMap mapStationIdToName(Set<Long> stationIds) {
		List<Station> stations = stationRepository.findAllById(stationIds);

		return StationIdNameMap.of(stations);
	}

	@Transactional
	public void delete(Member member, FavoriteDeleteRequest favoriteDeleteRequest) {
		Favorite favorite = toFavorite(favoriteDeleteRequest);
		Member persistMember = findMemberById(member)
			.removeFavorite(favorite.getSourceId(), favorite.getTargetId());

		memberRepository.save(persistMember);
	}

	private Member findMemberById(Member member) {
		return memberRepository.findById(member.getId())
			.orElseThrow(IllegalArgumentException::new);
	}

	private Long findStationIdByName(String stationName) {
		return stationRepository.findIdByName(stationName)
			.orElseThrow(IllegalArgumentException::new);
	}

	private Favorite toFavorite(FavoriteDeleteRequest favoriteDeleteRequest) {
		List<String> stationNames = Arrays.asList(favoriteDeleteRequest.getSource(),
			favoriteDeleteRequest.getTarget());
		List<Station> stations = stationRepository.findAllByName(stationNames);

		return Favorite.of(
			findSourceStationIdFromRequestInStations(favoriteDeleteRequest, stations),
			findTargetStationIdFromRequestInStations(favoriteDeleteRequest, stations));
	}

	private Long findSourceStationIdFromRequestInStations(
		FavoriteDeleteRequest favoriteDeleteRequest, List<Station> stations) {
		return stations.stream()
			.filter(station -> station.equalName(favoriteDeleteRequest.getSource()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("출발역을 찾을 수 없습니다."))
			.getId();
	}

	private Long findTargetStationIdFromRequestInStations(
		FavoriteDeleteRequest favoriteDeleteRequest, List<Station> stations) {
		return stations.stream()
			.filter(station -> station.equalName(favoriteDeleteRequest.getTarget()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("도착역을 찾을 수 없습니다."))
			.getId();
	}
}
