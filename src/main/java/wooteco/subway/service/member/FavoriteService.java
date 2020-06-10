package wooteco.subway.service.member;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@Service
public class FavoriteService {

	private final MemberRepository memberRepository;

	public FavoriteService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Transactional
	public void createFavorite(Member member, FavoriteRequest request) {
		Favorite favorite = request.toFavorite();

		member.addFavorite(favorite);
		memberRepository.save(member);
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findFavorites(Member member) {
		return FavoriteResponse.listOf(member.getFavorites());
	}

	@Transactional
	public void deleteFavorite(Member member, FavoriteRequest request) {
		member.removeFavorite(request.toFavorite());
		memberRepository.save(member);
	}

}
