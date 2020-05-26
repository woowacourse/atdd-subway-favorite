package wooteco.subway.service.member.favorite;


import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.NoMemberExistException;

@Service
public class FavoriteService {
	private MemberRepository memberRepository;

	public FavoriteService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public void addFavorite(long memberId, long sourceId, long targetId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(NoMemberExistException::new);

		member.addFavorite(Favorite.of(sourceId, targetId));
		memberRepository.save(member);
	}

	public void removeFavorite(long memberId, long sourceId, long targetId) {
		Member member = memberRepository.findById(memberId)
				.orElseThrow(NoMemberExistException::new);

		member.removeFavorite(Favorite.of(sourceId, targetId));
		memberRepository.save(member);
	}
}
