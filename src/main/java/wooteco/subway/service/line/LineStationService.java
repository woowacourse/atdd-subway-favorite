package wooteco.subway.service.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@Service
public class LineStationService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public LineDetailResponse findLineWithStationsById(Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new IllegalArgumentException("노선을 찾을수 없습니다."));
        List<Long> lineStationIds = line.getStationIds();
        List<Station> stations = stationRepository.findAllById(lineStationIds);

        return LineDetailResponse.of(line, mapStations(lineStationIds, stations));
    }

    @Transactional(readOnly = true)
    public WholeSubwayResponse findLinesWithStations() {
        Lines lines = new Lines(lineRepository.findAll());
        List<Station> stations = stationRepository.findAllById(lines.getStationIds());

        List<LineDetailResponse> lineDetailResponses = lines.getLines().stream()
            .map(line -> LineDetailResponse.of(line, mapStations(line.getStationIds(), stations)))
            .collect(Collectors.toList());

        return WholeSubwayResponse.of(lineDetailResponses);
    }

    private List<Station> mapStations(List<Long> stationsIds, List<Station> stations) {
        return stations.stream()
            .filter(station -> stationsIds.contains(station.getId()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineStationByStationId(Long stationId) {
        List<Line> lines = lineRepository.findAll();
        lines.forEach(line -> line.removeLineStationById(stationId));
        lineRepository.saveAll(lines);
    }
}
