package wooteco.subway.service.member;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.member.dto.FavoriteCreateRequest;

@Service
public class FavoritesService {
    private final MemberRepository memberRepository;

    public FavoritesService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void addFavorite(Member member, FavoriteCreateRequest favoriteCreateRequest) {
        // Favorite entity 추가하기
        // Member에 favorite 리스트 추가
        // favorite list는 일급 컬렉션으로 만들기
        memberRepository.save(member);
    }
}
