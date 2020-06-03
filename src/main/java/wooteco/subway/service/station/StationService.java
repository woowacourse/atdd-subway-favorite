package wooteco.subway.service.station;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.line.LineStationService;

@Service
public class StationService {
    private final StationRepository stationRepository;
    private final LineStationService lineStationService;
    private final FavoriteService favoriteService;

    public StationService(StationRepository stationRepository,
        LineStationService lineStationService,
        FavoriteService favoriteService) {
        this.stationRepository = stationRepository;
        this.lineStationService = lineStationService;
        this.favoriteService = favoriteService;
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        favoriteService.deleteByStationId(id);
        stationRepository.deleteById(id);
    }
}
