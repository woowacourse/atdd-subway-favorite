package wooteco.subway.service.path;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.exception.WrongStationException;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.StationService;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationService stationService;
    private final LineRepository lineRepository;
    private final GraphService graphService;

    public PathService(StationService stationService, LineRepository lineRepository, GraphService graphService) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.graphService = graphService;
    }

    public PathResponse findPath(String source, String target, PathType type) {
        if (Objects.equals(source, target)) {
            throw new WrongStationException("SAME_STATION");
        }

        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findStationByName(source);
        Station targetStation = stationService.findStationByName(target);

        List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), type);
        Stations stations = stationService.findStationsById(path);

        LineStations lineStations = lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .collect(Collectors.collectingAndThen(Collectors.toSet(), LineStations::new));

        LineStations paths = lineStations.extractPathLineStation(path);
        int duration = paths.getTotalDuration();
        int distance = paths.getTotalDistance();

        Stations pathStation = path.stream()
                .map(stations::extractStation)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Stations::new));

        return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
    }
}
