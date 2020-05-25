package wooteco.subway.service.favorite;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
        Favorite favorite = new Favorite(memberId, request.getSource(), request.getTarget());
        return FavoriteResponse.from(favoriteRepository.save(favorite));
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);

        return favorites.stream()
            .map(favorite -> FavoriteResponse.of(
                favorite,
                stationRepository.findNameById(favorite.getSourceStationId()),
                stationRepository.findNameById(favorite.getTargetStationId())))
            .collect(Collectors.toList());
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
