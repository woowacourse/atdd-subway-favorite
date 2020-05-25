package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.member.MemberService;

@Service
public class FavoriteService {

    private final MemberService memberService;

    public FavoriteService(MemberService memberService) {
        this.memberService = memberService;
    }

    public Member addToMember(Member member, FavoriteRequest request) {
        Favorite favorite = request.toFavorite();
        member.addFavorite(favorite);
        return memberService.save(member);
    }
}
