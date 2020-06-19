package wooteco.subway.service.line;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.linestation.LineStation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

@Service
@AllArgsConstructor
public class LineService {
    private LineStationService lineStationService;
    private LineRepository lineRepository;

    public LineResponse save(Line line) {
        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> findLines() {
        return LineResponse.listOf(lineRepository.findAll());
    }

    public void addLineStation(Long id, LineStationCreateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = lineStationService.findAllByStation(
            Arrays.asList(request.getPreStationId(), request.getStationId()));
        Station preStation = stations.stream()
            .filter(
                station -> Objects.nonNull(station) && station.isSameId(request.getPreStationId()))
            .findAny()
            .orElse(null);
        Station nextStation = stations.stream()
            .filter(station -> Objects.nonNull(station) && station.isSameId(request.getStationId()))
            .findAny()
            .orElseThrow(RuntimeException::new);
        LineStation lineStation = new LineStation(preStation, nextStation, request.getDistance(),
            request.getDuration());
        lineStation.applyLine(line);
        lineRepository.save(line);
    }

    public void updateLine(Long id, LineRequest request) {
        Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        persistLine.update(request.toLine());
        lineRepository.save(persistLine);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineDetailResponse retrieveLine(Long id) {
        return lineStationService.findLineWithStationsById(id);
    }

    public WholeSubwayResponse findAllLines() {
        List<LineDetailResponse> responses = lineRepository.findAll().stream()
            .map(line -> {
                List<Station> stations = line.getStations()
                    .stream()
                    .map(LineStation::getNextStation)
                    .collect(Collectors.toList());
                return LineDetailResponse.of(line, stations);
            }).collect(Collectors.toList());

        return WholeSubwayResponse.of(responses);
    }
}
