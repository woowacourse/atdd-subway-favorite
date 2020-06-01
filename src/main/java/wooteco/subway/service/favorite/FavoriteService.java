package wooteco.subway.service.favorite;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
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
		Member persistMember = findMemberById(member);

		Long source = findStationIdByName(favoriteCreateRequest.getSource());
		Long target = findStationIdByName(favoriteCreateRequest.getTarget());
		Favorite favorite = Favorite.of(source, target);

		persistMember.addFavorite(favorite);

		memberRepository.save(persistMember);

	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> find(Member member) {
		Member persistMember = findMemberById(member);
		Set<Favorite> favorites = persistMember.getFavorites();

		return favorites.stream()
			.map(this::toFavoriteResponse)
			.collect(Collectors.toList());
	}

	private FavoriteResponse toFavoriteResponse(Favorite favorite) {
		return new FavoriteResponse(
			findStationNameById(favorite.getSource()),
			findStationNameById(favorite.getTarget()));
	}

	@Transactional
	public void delete(Member member, FavoriteDeleteRequest favoriteDeleteRequest) {
		Favorite favorite = toFavorite(favoriteDeleteRequest);
		Member persistMember = findMemberById(member);

		persistMember.removeFavorite(favorite.getSource(), favorite.getTarget());

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

	private String findStationNameById(long stationId) {
		return stationRepository.findNameById(stationId)
			.orElseThrow(IllegalArgumentException::new);
	}

	private Favorite toFavorite(FavoriteDeleteRequest favoriteDeleteRequest) {
		return Favorite.of(
			stationRepository.findIdByName(favoriteDeleteRequest.getSource())
				.orElseThrow(IllegalArgumentException::new),
			stationRepository.findIdByName(favoriteDeleteRequest.getTarget())
				.orElseThrow(IllegalArgumentException::new));
	}
}
