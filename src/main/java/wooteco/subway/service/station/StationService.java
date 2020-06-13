package wooteco.subway.service.station;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.LineStationService;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {
    private LineStationService lineStationService;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public StationService(LineStationService lineStationService, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.lineStationService = lineStationService;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        deleteFavoriteIfRelatedToStationId(id);

        stationRepository.deleteById(id);
    }

    private void deleteFavoriteIfRelatedToStationId(Long id) {
        Optional<Favorite> favoriteBySourceId = favoriteRepository.findBySourceId(id);
        favoriteBySourceId.ifPresent(favorite ->
                favoriteRepository.deleteBySourceId(favorite.getSourceId()));

        Optional<Favorite> favoriteByTargetId = favoriteRepository.findByTargetId(id);
        favoriteByTargetId.ifPresent(favorite ->
                favoriteRepository.deleteByTargetId(favorite.getTargetId()));
    }
}
