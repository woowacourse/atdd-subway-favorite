package wooteco.subway.service.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.exception.WrongStationException;
import wooteco.subway.service.line.LineStationService;

import java.util.List;

@Service
public class StationService {
    private LineStationService lineStationService;
    private StationRepository stationRepository;

    public StationService(LineStationService lineStationService, StationRepository stationRepository) {
        this.lineStationService = lineStationService;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Station createStation(Station station) {
        return stationRepository.save(station);
    }

    @Transactional
    public void deleteStationById(Long id) {
        lineStationService.deleteLineStationByStationId(id);
        stationRepository.deleteById(id);
    }

    public List<Station> findStations() {
        return stationRepository.findAll();
    }

    public Station findStationByName(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(WrongStationException::new);
    }

    public List<Station> findStationsById(List<Long> ids) {
        return stationRepository.findAllById(ids);
    }
}
