package wooteco.subway.service.favorite;

import static wooteco.subway.exception.InvalidMemberException.NOT_FOUND_MEMBER;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.exception.InvalidMemberException;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteListResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse addFavorite(Member member,
        FavoriteCreateRequest favoriteCreateRequest) {
        member.addFavorite(favoriteCreateRequest.toFavorite());
        memberRepository.save(member);
        return FavoriteResponse.of(member.findFavorite(favoriteCreateRequest.getDepartureId(),
            favoriteCreateRequest.getDestinationId()));
    }

    public FavoriteListResponse findFavorites(Member member) {
        Member persistMember = memberRepository.findById(member.getId())
            .orElseThrow(() -> new InvalidMemberException(NOT_FOUND_MEMBER,member.getEmail()));
        return FavoriteListResponse.of(persistMember.getFavorites());
    }

    public void deleteFavorite(Member member, Long favoriteId) {
        member.deleteFavorite(favoriteId);
        memberRepository.save(member);
    }
}
