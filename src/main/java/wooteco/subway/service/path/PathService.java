package wooteco.subway.service.path;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.path.PathCalculator;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.StationService;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPath(String source, String target, PathType type) {
        if (Objects.equals(source, target)) {
            throw new DuplicatedStationException();
        }

        List<Line> lines = lineService.findLines();
        Station sourceStation = stationService.findByName(source);
        Station targetStation = stationService.findByName(target);

        PathCalculator pathCalculator = new PathCalculator();
        List<Long> path = pathCalculator.findPath(lines, sourceStation.getId(), targetStation.getId(), type);
        List<Station> stations = stationService.findAllById(path);

        List<LineStation> lineStations = lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .collect(Collectors.toList());

        LineStations paths = new LineStations(pathCalculator.extractPathLineStation(path, lineStations));
        int duration = paths.getTotalDuration();
        int distance = paths.getTotalDistance();

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
}