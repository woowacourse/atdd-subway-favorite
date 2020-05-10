package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {
    private StationRepository stationRepository;
    private LineRepository lineRepository;
    private GraphService graphService;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, GraphService graphService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.graphService = graphService;
    }

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
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .collect(Collectors.toList());

        List<LineStation> paths = extractPathLineStation(path, lineStations);
        int duration = paths.stream().mapToInt(it -> it.getDuration()).sum();
        int distance = paths.stream().mapToInt(it -> it.getDistance()).sum();

        List<Station> pathStation = path.stream()
                .map(it -> extractStation(it, stations))
                .collect(Collectors.toList());

        return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
    }

    private Station extractStation(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(it -> it.getId() == stationId)
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
                    .filter(it -> it.isLineStationOf(finalPreStationId, stationId))
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            paths.add(lineStation);
            preStationId = stationId;
        }

        return paths;
    }
}
