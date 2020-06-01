package wooteco.subway.service.path;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.web.exception.NoSuchValueException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static wooteco.subway.web.exception.NoSuchValueException.NO_SUCH_LINE_STATION_MESSAGE;
import static wooteco.subway.web.exception.NoSuchValueException.NO_SUCH_STATION_MESSAGE;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final GraphService graphService;

    public PathService(StationRepository stationRepository, LineRepository lineRepository,
                       GraphService graphService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.graphService = graphService;
    }

    public PathResponse findPath(String source, String target, PathType type) {
        if (Objects.equals(source, target)) {
            throw new RuntimeException();
        }

        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationRepository.findByName(source)
                .orElseThrow(() -> new NoSuchValueException(NO_SUCH_STATION_MESSAGE));
        Station targetStation = stationRepository.findByName(target)
                .orElseThrow(() -> new NoSuchValueException(NO_SUCH_STATION_MESSAGE));

        List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(),
                type);
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
                .orElseThrow(() -> new NoSuchValueException(NO_SUCH_STATION_MESSAGE));
    }

    private List<LineStation> extractPathLineStation(List<Long> path,
                                                     List<LineStation> lineStations) {
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
                    .orElseThrow(() -> new NoSuchValueException(NO_SUCH_LINE_STATION_MESSAGE));

            paths.add(lineStation);
            preStationId = stationId;
        }

        return paths;
    }
}
