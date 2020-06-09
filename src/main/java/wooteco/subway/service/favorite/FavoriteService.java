package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public List<FavoriteResponse> findAllFavoriteResponses(Long memberId) {
        Favorites favorites = new Favorites(favoriteRepository.findAllByMemberId(memberId));
        List<Long> stationIds = favorites.getAllSourceTargetStationIds();
        Stations stations = new Stations(stationRepository.findAllById(stationIds));

        return favorites.toFavoriteResponses(stations.toNamesMap());
    }

    public void createFavorite(Long memberId, FavoriteCreateRequest request) {
        Long sourceStationId = stationRepository.findIdByName(request.getSourceStationName());
        Long targetStationId = stationRepository.findIdByName(request.getTargetStationName());
        Favorite favorite = new Favorite(memberId, sourceStationId, targetStationId);
        favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }
}
