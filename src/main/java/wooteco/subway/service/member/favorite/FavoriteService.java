package wooteco.subway.service.member.favorite;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.FavoriteDetail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.NoMemberExistException;

import java.util.List;
import java.util.Set;

@Service
public class FavoriteService {
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;

	public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public Favorite addFavorite(long memberId, long sourceId, long targetId) {
		Member member = getMember(memberId);
		Favorite addedFavorite = member.addFavorite(sourceId, targetId);
		memberRepository.save(member);

		return addedFavorite;
	}

	private Member getMember(long memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(NoMemberExistException::new);
	}

	@Transactional(readOnly = true)
	public List<FavoriteDetail> readFavorites(long memberId) {
		Member member = getMember(memberId);

		Set<Long> ids = member.getFavoriteStationIds();
		Stations stations = Stations.of(stationRepository.findAllById(ids));

		return member.getFavoriteDetails(stations, memberId);
	}

	@Transactional
	public void removeFavorite(long memberId, long sourceId, long targetId) {
		Member member = getMember(memberId);

		member.removeFavorite(Favorite.of(sourceId, targetId));
		memberRepository.save(member);
	}
}
