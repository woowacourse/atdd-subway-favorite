package wooteco.subway.service.member.favorite;


import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.NoMemberExistException;

@Service
public class FavoriteService {
	private MemberRepository memberRepository;

	public FavoriteService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public Favorite addFavorite(long memberId, long sourceId, long targetId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(NoMemberExistException::new);

		Favorite favorite = Favorite.of(sourceId, targetId);
		member.addFavorite(favorite);
		memberRepository.save(member);

		return favorite;
	}

	public Favorites readFavorites(long memberId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(NoMemberExistException::new);

		return member.getFavorites();
	}

	public void removeFavorite(long memberId, long sourceId, long targetId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(NoMemberExistException::new);

		member.removeFavorite(Favorite.of(sourceId, targetId));
		memberRepository.save(member);
	}
}
