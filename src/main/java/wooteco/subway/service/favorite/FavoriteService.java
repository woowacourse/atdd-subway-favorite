package wooteco.subway.service.favorite;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NoSuchAccountException;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Transactional
@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberRepository memberRepository,
        final StationRepository stationRepository,
        final FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findAllFavoritesByMember(Member member) {
        return member.getFavorites().stream()
            .map(favorite ->
                new FavoriteResponse(favorite.getId(),
                    favorite.getSourceStation().getName(),
                    favorite.getTargetStation().getName())
            ).collect(Collectors.toList());
    }

    public Member saveFavorite(Long id, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchAccountException::new);
        member.addFavorite(new Favorite(
            stationRepository.findById(favoriteRequest.getSourceStationId())
                .orElseThrow(NoSuchAccountException::new),
            stationRepository.findById(favoriteRequest.getTargetStationId())
                .orElseThrow(NoSuchAccountException::new)
        ));
        return memberRepository.save(member);
    }

    public void deleteFavorite(Member member, Long favoriteId) {
        if (member.doesNotHaveFavoriteWithId(favoriteId)) {
            throw new IllegalArgumentException("잘못된 즐겨찾기 삭제 요청입니다.");
        }
        Favorite favorite = favoriteRepository.findById(favoriteId).get();
        member.removeFavorite(favorite);
    }
}
