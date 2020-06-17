package wooteco.subway.service.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.linestation.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;

@Service
public class LineStationService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineDetailResponse findLineWithStationsById(Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        List<Station> stations = line.getStations()
            .getStations().stream()
            .map(LineStation::getNextStation)
            .collect(Collectors.toList());

        return LineDetailResponse.of(line, stations);
    }

    public List<Station> findAllByStation(Iterable stations) {
        return stationRepository.findAllById(stations);
    }

    private List<Station> mapStations(List<Long> stationsIds, List<Station> stations) {
        return stations.stream()
            .filter(it -> stationsIds.contains(it.getId()))
            .collect(Collectors.toList());
    }

    public void deleteLineStationByStationId(Long stationId) {
        List<Line> lines = lineRepository.findAll();
        lines.stream().forEach(it -> it.removeLineStationById(stationId));
        lineRepository.saveAll(lines);
    }
}
