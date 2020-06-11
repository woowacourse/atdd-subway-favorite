package wooteco.subway.service.station;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.LineStationService;

@Service
public class StationService {
    private LineStationService lineStationService;
    private StationRepository stationRepository;

    public StationService(LineStationService lineStationService,
        StationRepository stationRepository) {
        this.lineStationService = lineStationService;
        this.stationRepository = stationRepository;
    }

    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    public List<Long> findIdsByNames(List<String> names) {
        return stationRepository.findIdsByNames(names);
    }

    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        stationRepository.deleteById(id);
    }

    public List<Station> findAllById(Collection<Long> ids) {
        return stationRepository.findAllById(ids);
    }
}
