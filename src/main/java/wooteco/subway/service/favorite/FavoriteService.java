package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponses;

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

    public FavoriteResponses findAllFavoriteResponses(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
        List<Long> sourceStationIds = new ArrayList<>();
        List<Long> targetStationIds = new ArrayList<>();
        for (Favorite favorite : favorites) {
            sourceStationIds.add(favorite.getSourceStationId());
            targetStationIds.add(favorite.getTargetStationId());
        }

        List<Station> sourceStations = stationRepository.findAllById(sourceStationIds);
        List<Station> targetStations = stationRepository.findAllById(targetStationIds);

        return FavoriteResponses.of(favorites, sourceStations, targetStations);
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
