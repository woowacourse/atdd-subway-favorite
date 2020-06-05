package wooteco.subway.service.path;

import java.util.ArrayList;
import java.util.List;
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
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.SameSourceTargetException;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final GraphService graphService;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, GraphService graphService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.graphService = graphService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(String source, String target, PathType type) {
        if (Objects.equals(source, target)) {
            throw new SameSourceTargetException("입력한 출발역과 도착역이 같습니다.");
        }

        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationRepository.findByName(source)
                .orElseThrow(() -> new EntityNotFoundException("역을 찾을 수 없습니다."));
        Station targetStation = stationRepository.findByName(target)
                .orElseThrow(() -> new EntityNotFoundException("역을 찾을 수 없습니다."));

        List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), type);
        List<Station> stations = stationRepository.findAllById(path);

        List<LineStation> lineStations = lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .collect(Collectors.toList());

        List<LineStation> paths = extractPathLineStation(path, lineStations);
        int duration = paths.stream().mapToInt(LineStation::getDuration).sum();
        int distance = paths.stream().mapToInt(LineStation::getDistance).sum();

        List<Station> pathStation = path.stream()
                .map(stationId -> extractStation(stationId, stations))
                .collect(Collectors.toList());

        return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
    }

    private Station extractStation(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("역을 찾을 수 없습니다."));
    }

    private List<LineStation> extractPathLineStation(List<Long> path, List<LineStation> lineStations) {
        Long preStationId = null;
        List<LineStation> paths = new ArrayList<>();

        for (Long stationId : path) {
            if (preStationId == null) {
                preStationId = stationId;
                continue;
            }

            Long finalPreStationId = preStationId;
            LineStation lineStation = lineStations.stream()
                    .filter(lineStation1 -> lineStation1.isLineStationOf(finalPreStationId, stationId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("노선에 속한 역이 없습니다."));

            paths.add(lineStation);
            preStationId = stationId;
        }

        return paths;
    }
}
