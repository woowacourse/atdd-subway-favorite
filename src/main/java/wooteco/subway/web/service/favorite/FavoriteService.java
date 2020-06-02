package wooteco.subway.web.service.favorite;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.station.StationService;
import wooteco.subway.web.exception.NotFoundFavoriteException;
import wooteco.subway.web.service.favorite.dto.FavoriteDetailResponse;
import wooteco.subway.web.service.favorite.dto.FavoriteRequest;
import wooteco.subway.web.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.service.member.MemberService;

@Service
@Transactional
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService,
        FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse create(Member member, FavoriteRequest request) {
        Favorite favorite = makeFavorite(member, request);
        return FavoriteResponse.of(favoriteRepository.save(favorite));
    }

    public void delete(Member member, Long id) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(id, member.getId())
            .orElseThrow(() -> new NotFoundFavoriteException(id));
        favoriteRepository.delete(favorite);
    }

    public Set<FavoriteDetailResponse> getAll(Member member) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        Set<Long> ids = new HashSet<>();
        for (Favorite favorite : favorites) {
            ids.add(favorite.getSourceId());
            ids.add(favorite.getTargetId());
        }
        List<Station> stations = stationService.findAllById(ids);
        Map<Long, String> idToName = stations.stream()
            .collect(Collectors.toMap(Station::getId, Station::getName));

        return favorites.stream()
            .map(favorite -> new FavoriteDetailResponse(favorite.getId(), favorite.getMemberId(),
                favorite.getSourceId(), favorite.getTargetId(),
                idToName.get(favorite.getSourceId()),
                idToName.get(favorite.getTargetId())))
            .collect(Collectors.toSet());
    }

    private Favorite makeFavorite(Member member, FavoriteRequest request) {
        Long sourceId = stationService.findStationIdByName(request.getSourceName());
        Long targetId = stationService.findStationIdByName(request.getTargetName());

        return Favorite.of(member.getId(), sourceId, targetId);
    }
}
