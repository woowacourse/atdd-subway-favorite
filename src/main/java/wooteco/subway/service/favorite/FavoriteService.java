package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Favorite createFavorite(Member member, FavoriteRequest favoriteRequest) {
        Favorite favorite = new Favorite(favoriteRequest.getPreStationId(),
            favoriteRequest.getStationId());
        member.addFavorite(favorite);
        Member updatedMember = memberRepository.save(member);
        return updatedMember.findEqualFavoriteTo(favorite);
    }
}
