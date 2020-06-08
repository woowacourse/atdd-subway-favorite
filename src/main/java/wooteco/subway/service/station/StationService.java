package wooteco.subway.service.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exceptions.NotExistStationException;
import wooteco.subway.service.line.LineStationService;

import java.util.List;

@Service
@Transactional
public class StationService {
    private LineStationService lineStationService;
    private StationRepository stationRepository;

    public StationService(LineStationService lineStationService, StationRepository stationRepository) {
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
        stationRepository.deleteById(id);
    }

    public Station findStationByName(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new NotExistStationException(name));
    }

    public Stations findStationsByIds(List<Long> stationsIds) {
        List<Station> stations = stationRepository.findAllById(stationsIds);
        return Stations.from(stations);
    }
}
