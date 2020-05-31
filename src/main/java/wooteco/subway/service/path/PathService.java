package wooteco.subway.service.path;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NoLineStationExistsException;
import wooteco.subway.exception.NoPathExistsException;
import wooteco.subway.exception.NoStationExistsException;
import wooteco.subway.exception.SourceEqualsTargetException;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

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

    public PathResponse findPath(String source, String target, PathType type) {
        if (Objects.equals(source, target)) {
            throw new SourceEqualsTargetException();
        }

        Lines lines = Lines.of(lineRepository.findAll());
        Station sourceStation = stationRepository.findByName(source).orElseThrow(NoStationExistsException::new);
        Station targetStation = stationRepository.findByName(target).orElseThrow(NoStationExistsException::new);

        List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), type);

        if (path.isEmpty()) {
            throw new NoPathExistsException();
        }

        List<Station> stations = stationRepository.findAllById(path);

        List<LineStation> lineStations = lines.getLines().stream()
                .flatMap(line -> line.getStations().stream())
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .collect(Collectors.toList());

        List<LineStation> paths = extractPathLineStation(path, lineStations);
        int duration = paths.stream().mapToInt(LineStation::getDuration).sum();
        int distance = paths.stream().mapToInt(LineStation::getDistance).sum();

        List<Station> pathStation = path.stream()
                .map(it -> extractStation(it, stations))
                .collect(Collectors.toList());

        return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
    }

    private Station extractStation(Long stationId, List<Station> stations) {
        return stations.stream()
                .filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(NoStationExistsException::new);
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
            LineStation foundLineStation = lineStations.stream()
                    .filter(lineStation -> lineStation.isLineStationOf(finalPreStationId, stationId))
                    .findFirst()
                    .orElseThrow(NoLineStationExistsException::new);

            paths.add(foundLineStation);
            preStationId = stationId;
        }

        return paths;
    }
}
