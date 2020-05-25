package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse addFavorite(Member member,
        FavoriteCreateRequest favoriteCreateRequest) {
        member.addFavorite(favoriteCreateRequest.getDepartureId(),
            favoriteCreateRequest.getDestinationId());
        memberRepository.save(member);
        return FavoriteResponse.of(member.findFavorite(favoriteCreateRequest.getDepartureId(),
            favoriteCreateRequest.getDestinationId()));
    }
}
