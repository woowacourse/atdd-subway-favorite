package wooteco.subway.service.favorite;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.favorite.dto.FavoriteDetailResponse;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.StationService;

@Service
public class FavoriteService {
    private static final int SOURCE_ID_INDEX = 0;
    private static final int TARGET_ID_INDEX = 1;

    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(MemberService memberService,
        StationService stationService) {
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public Member addToMember(Member member, FavoriteRequest request) {
        Favorite favorite = getFavorite(request);
        member.addFavorite(favorite);
        return memberService.save(member);
    }

    public void deleteById(Member member, Long id) {
        member.deleteFavorite(id);
        memberService.save(member);
    }

    private Favorite getFavorite(FavoriteRequest request) {
        List<Long> ids = stationService.findIdsByNames(request.toList());
        return Favorite.of(ids.get(SOURCE_ID_INDEX), ids.get(TARGET_ID_INDEX));
    }

    public Set<FavoriteDetailResponse> getAll(Member member) {
        Set<Favorite> favorites = member.getFavorites();
        Set<Long> ids = new HashSet<>();
        for (Favorite favorite : favorites) {
            ids.add(favorite.getSourceId());
            ids.add(favorite.getTargetId());
        }
        List<Station> stations = stationService.findAllById(ids);
        Map<Long, String> idToName = stations.stream()
            .collect(Collectors.toMap(Station::getId, Station::getName));

        return favorites.stream()
            .map(favorite -> new FavoriteDetailResponse(favorite.getId(), favorite.getSourceId(),
                favorite.getTargetId(), idToName.get(favorite.getSourceId()), idToName.get(favorite.getTargetId())))
            .collect(Collectors.toSet());
    }
}
