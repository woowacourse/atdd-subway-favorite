package wooteco.subway.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineStationService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineStationService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public LineDetailResponse findLineWithStationsById(Long lineId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        List<Long> lineStationIds = line.getStationIds();
        List<Station> stations = stationRepository.findAllById(lineStationIds);

        return LineDetailResponse.of(line, mapStations(lineStationIds, stations));
    }

    @Transactional(readOnly = true)
    public WholeSubwayResponse findLinesWithStations() {
        Lines lines = new Lines(lineRepository.findAll());
        List<Station> stations = stationRepository.findAllById(lines.getStationIds());

        List<LineDetailResponse> lineDetailResponses = lines.getLines().stream()
                .map(it -> LineDetailResponse.of(it, mapStations(it.getStationIds(), stations)))
                .collect(Collectors.toList());

        return WholeSubwayResponse.of(lineDetailResponses);
    }

    private List<Station> mapStations(List<Long> stationsIds, List<Station> stations) {
        return stations.stream()
                .filter(it -> stationsIds.contains(it.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineStationByStationId(Long stationId) {
        List<Line> lines = lineRepository.findAll();
        lines.stream().forEach(it -> it.removeLineStationById(stationId));
        lineRepository.saveAll(lines);
    }
}
