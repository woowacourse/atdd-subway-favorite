package wooteco.subway.service.station;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.line.LineStationService;

import java.util.List;

@Service
public class StationService {
    private LineStationService lineStationService;
    private FavoriteService favoriteService;
    private StationRepository stationRepository;

    public StationService(LineStationService lineStationService, FavoriteService favoriteService, StationRepository stationRepository) {
        this.lineStationService = lineStationService;
        this.favoriteService = favoriteService;
        this.stationRepository = stationRepository;
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(NoExistStationException::new);
    }

    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        favoriteService.deleteFavoriteByStationId(id);
        stationRepository.deleteById(id);
    }
}
