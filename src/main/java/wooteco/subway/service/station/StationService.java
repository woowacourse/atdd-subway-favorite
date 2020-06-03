package wooteco.subway.service.station;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.FavoritePathService;
import wooteco.subway.service.line.LineStationService;

@Service
public class StationService {
    private FavoritePathService favoritePathService;
    private LineStationService lineStationService;
    private StationRepository stationRepository;

    public StationService(FavoritePathService favoritePathService,
        LineStationService lineStationService, StationRepository stationRepository) {
        this.favoritePathService = favoritePathService;
        this.lineStationService = lineStationService;
        this.stationRepository = stationRepository;
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        favoritePathService.deletePathByStation(id);
        stationRepository.deleteById(id);
    }
}
