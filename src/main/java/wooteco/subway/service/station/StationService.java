package wooteco.subway.service.station;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.LineStationService;

import java.util.List;

@Service
public class StationService {
    public static final String STATION_NOT_FOUND_MESSAGE = "존재하지 않는 역 입니다.";
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

    public Station findByName(String stationName) {
        return stationRepository.findByName(stationName)
            .orElseThrow(() -> new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE));
    }

    public String findNameById(Long stationId) {
        Station station = stationRepository.findById(stationId).orElseThrow(() ->
            new IllegalArgumentException(STATION_NOT_FOUND_MESSAGE));
        return station.getName();
    }
}
