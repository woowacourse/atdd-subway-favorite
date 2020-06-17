package wooteco.subway.service.favorite;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.station.dto.StationResponse;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public Long addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        return favoriteRepository.save(favoriteRequest.toFavorite(memberId)).getId();
    }

    public FavoriteResponse getFavorite(Long memberId, Long source, Long target) {
        Favorite favorite = favoriteRepository.findBySourceAndTargetAndMember(memberId, source,
            target)
            .orElseThrow(() -> new IllegalArgumentException("해당 즐겨찾기가 없습니다."));
        return toFavoriteResponse(favorite);
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        return favorites.stream()
            .map(this::toFavoriteResponse)
            .collect(Collectors.toList());
    }

    private FavoriteResponse toFavoriteResponse(Favorite favorite) {
        List<Station> stations = stationRepository.findAllById(
            Arrays.asList(favorite.getSource(), favorite.getTarget()));

        Map<Long, StationResponse> stationMatcher = stations.stream()
            .collect(Collectors.toMap(
                Station::getId, StationResponse::of
            ));

        return new FavoriteResponse(favorite.getId(), stationMatcher.get(favorite.getSource()),
            stationMatcher.get(favorite.getTarget()));
    }

    public void removeFavorite(Long memberId, Long source, Long target) {
        boolean isDeleted = favoriteRepository.deleteByMemberIdAndSourceAndTarget(memberId, source,
            target);
        if (!isDeleted) {
            throw new IllegalArgumentException("삭제에 실패했습니다!");
        }
    }

    public void removeFavoriteById(Long memberId, Long favoriteId) {
        boolean isDeleted = favoriteRepository.deleteByIdWithMemberId(memberId, favoriteId);
        if (!isDeleted) {
            throw new IllegalArgumentException("삭제에 실패했습니다!");
        }
    }
}
