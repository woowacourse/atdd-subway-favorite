package wooteco.subway.service.member.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.service.member.favorite.dto.FavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;

import java.util.List;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void createFavorite(Member member, FavoriteRequest request) {
        Favorite favorite = request.toFavorite();
        member.addFavorite(favorite);
        memberRepository.save(member);
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        return FavoriteResponse.listOf(member.getFavorites());
    }

    public void deleteFavorite(Member member, FavoriteRequest request) {
        member.removeFavorite(request.toFavorite());
        memberRepository.save(member);
    }
}
