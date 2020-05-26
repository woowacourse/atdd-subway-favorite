package wooteco.subway.service.station;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NotFoundStationException;
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

    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        stationRepository.deleteById(id);
    }

    public Long findStationIdByName(String name) {
        return stationRepository.findByName(name)
            .map(Station::getId)
            .orElseThrow(() -> new NotFoundStationException(name + "역을 찾을 수 없습니다."));
    }

    public List<Station> findAllById(Collection<Long> ids) {
        return stationRepository.findAllById(ids);
    }
}
