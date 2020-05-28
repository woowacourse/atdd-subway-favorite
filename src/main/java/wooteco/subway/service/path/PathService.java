package wooteco.subway.service.path;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Transactional
    public PathResponse findPath(String source, String target, PathType type) {
        if (Objects.equals(source, target)) {
            throw new RuntimeException();
        }

        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationRepository.findByName(source).orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findByName(target).orElseThrow(RuntimeException::new);

        List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), type);
        List<Station> stations = stationRepository.findAllById(path);

        List<LineStation> lineStations = lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(station -> Objects.nonNull(station.getPreStationId()))
                .collect(Collectors.toList());

        List<LineStation> paths = extractPathLineStation(path, lineStations);
        int duration = paths.stream().mapToInt(LineStation::getDuration).sum();
        int distance = paths.stream().mapToInt(LineStation::getDistance).sum();

        List<Station> pathStation = path.stream()
                .map(lineStation -> extractStation(lineStation, stations))
                .collect(Collectors.toList());

        return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
    }

    private Station extractStation(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(station -> Objects.equals(station.getId(), stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
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
                    .filter(lineStationValue -> lineStationValue.isLineStationOf(finalPreStationId, stationId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("해당 노선 경로가 없습니다."));

            paths.add(lineStation);
            preStationId = stationId;
        }

        return paths;
    }
}
