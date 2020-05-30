package wooteco.subway.service.path;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.web.controlleradvice.exception.EntityNotFoundException;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final Graphs graphs;

    public PathService(StationRepository stationRepository, LineRepository lineRepository,
        Graphs graphs) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.graphs = graphs;
        graphs.initialize(lineRepository.findAll(), stationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Station::getId, station -> station));
        List<LineDetailResponse> responses = lines.stream()
            .map(line -> getLineDetailResponse(stations, line))
            .collect(Collectors.toList());
        return WholeSubwayResponse.of(responses);
    }

    @Transactional(readOnly = true)
    public LineDetailResponse findLineWithStationsById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        List<Station> stations = stationRepository.findAllById(line.getStationIds());
        return LineDetailResponse.of(line, stations);
    }

    @Transactional
    public void addLineStation(Long id, LineStationCreateRequest request) {
        validateStations(request.getPreStationId(), request.getStationId());
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        LineStation lineStation = LineStation.of(request.getPreStationId(), request.getStationId(),
            request.getDistance(), request.getDuration());
        line.addLineStation(lineStation);

        lineRepository.save(line);
        graphs.initialize(lineRepository.findAll(), stationRepository.findAll());
    }

    @Transactional
    public void removeLineStation(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
        line.removeLineStationById(stationId);
        lineRepository.save(line);
        graphs.initialize(lineRepository.findAll(), stationRepository.findAll());
    }

    @Transactional
    public PathResponse findPath(Long sourceId, Long targetId, PathType pathType) {
        validate(sourceId, targetId);
        return graphs.findPath(sourceId, targetId, pathType);
    }

    private void validate(Long sourceId, Long targetId) {
        validateEmpty(sourceId, targetId);
        validateSameIds(sourceId, targetId);
    }

    private void validateEmpty(Long sourceId, Long targetId) {
        if (Objects.isNull(sourceId) || Objects.isNull(targetId)) {
            throw new IllegalArgumentException("소스와 타겟 정보가 비어있습니다.");
        }
    }

    private void validateSameIds(Long sourceId, Long targetId) {
        if (Objects.equals(sourceId, targetId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private LineDetailResponse getLineDetailResponse(Map<Long, Station> stations, Line line) {
        List<Long> stationIds = line.getStationIds();
        List<Station> stationsList = stationIds.stream()
            .map(stations::get)
            .collect(Collectors.toList());
        return LineDetailResponse.of(line, stationsList);
    }

    private void validateStations(Long preStationId, Long stationId) {
        if (Objects.nonNull(preStationId) && !stationRepository.existsById(preStationId)) {
            throw new EntityNotFoundException(Station.class.getName(), preStationId);
        }
        if (Objects.isNull(stationId) || !stationRepository.existsById(stationId)) {
            throw new EntityNotFoundException(Station.class.getName(), stationId);
        }
    }
}
