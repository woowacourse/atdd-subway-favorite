package wooteco.subway.service.favorite;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NoSuchAccountException;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(final MemberRepository memberRepository,
        final StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> findAllFavoritesByMember(Member member) {
        Map<Long, String> stationNameById = stationRepository.findAll().stream()
            .collect(Collectors.toMap(Station::getId, Station::getName));

        return member.getFavorites().stream()
            .map(favorite ->
                new FavoriteResponse(favorite.getId(),
                    stationNameById.get(favorite.getSourceStationId()),
                    stationNameById.get(favorite.getTargetStationId()))
            ).collect(Collectors.toList());
    }

    public Member saveFavorite(Long id, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchAccountException::new);
        member.addFavorite(Favorite.of(favoriteRequest));
        return memberRepository.save(member);
    }

    public void deleteFavorite(Member member, Long favoriteId) {
        if (member.doesNotHaveFavoriteWithId(favoriteId)) {
            throw new IllegalArgumentException("잘못된 즐겨찾기 삭제 요청입니다.");
        }
        memberRepository.deleteFavoriteById(favoriteId);
    }
}
