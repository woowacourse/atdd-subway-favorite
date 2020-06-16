package wooteco.subway.service.line;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@Service
@AllArgsConstructor
public class LineService {
    private LineStationService lineStationService;
    private LineRepository lineRepository;

    public Line save(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = lineStationService.findAllByStation(
            Arrays.asList(request.getPreStationId(),
                request.getStationId()));
        LineStation lineStation = new LineStation(stations.get(0), stations.get(1),
            request.getDistance(), request.getDuration(), line);
        line.addLineStation(lineStation);

        lineRepository.save(line);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineDetailResponse retrieveLine(Long id) {
        return lineStationService.findLineWithStationsById(id);
    }

    public WholeSubwayResponse findAllLines() {
        List<LineDetailResponse> responses = lineRepository.findAll().stream().map(line -> {
            List<Station> stations = line.getStations()
                .stream()
                .map(LineStation::getNextStation)
                .collect(Collectors.toList());
            return LineDetailResponse.of(line, stations);
        }).collect(Collectors.toList());

        return WholeSubwayResponse.of(responses);
    }
}
