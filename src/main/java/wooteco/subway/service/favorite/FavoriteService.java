package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.exception.NoSuchStationException;

import java.util.ArrayList;
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
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        for (Favorite favorite : favorites) {
            Station sourceStation = stationRepository.findById(favorite.getSourceStationId()).orElseThrow(NoSuchStationException::new);
            Station targetStation = stationRepository.findById(favorite.getTargetStationId()).orElseThrow(NoSuchStationException::new);
            String sourceStationName = sourceStation.getName();
            String targetStationName = targetStation.getName();
            favoriteResponses.add(FavoriteResponse.of(favorite, sourceStationName, targetStationName));
        }
        return favoriteResponses;

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
