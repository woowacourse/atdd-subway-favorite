package woowa.bossdog.subway.service.path;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import woowa.bossdog.subway.domain.Line;
import woowa.bossdog.subway.domain.LineStation;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.LineRepository;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.path.dto.PathRequest;
import woowa.bossdog.subway.service.path.dto.PathResponse;
import woowa.bossdog.subway.service.path.dto.PathType;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final GraphService graphService;

    public PathResponse findPath(final PathRequest request) {
        String source = request.getSource();
        String target = request.getTarget();
        PathType type = request.getType();

        if (Objects.equals(source, target)) {
            throw new DuplicatedStationPathException();
        }

        Station sourceStation = stationRepository.findByName(source)
                .orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findByName(target)
                .orElseThrow(NoSuchElementException::new);

        final List<Line> lines = lineRepository.findAll();
        final List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), type);

        List<LineStation> paths = extractPathLineStation(path, lines);
        int duration = paths.stream().mapToInt(LineStation::getDuration).sum();
        int distance = paths.stream().mapToInt(LineStation::getDistance).sum();

        List<Station> pathStations = getPathStations(path);
        return new PathResponse(StationResponse.listFrom(pathStations), duration, distance);
    }

    private List<Station> getPathStations(final List<Long> path) {
        final List<Station> stations = stationRepository.findAllById(path);
        return path.stream()
                .map(it -> extractStation(it, stations))
                .collect(Collectors.toList());
    }

    private Station extractStation(final Long stationId, final List<Station> stations) {
        return stations.stream()
                .filter(it -> Objects.equals(it.getId(), stationId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private List<LineStation> extractPathLineStation(final List<Long> path, final List<Line> lines) {
        final List<LineStation> pathLineStations = new ArrayList<>();

        List<LineStation> lineStations = lines.stream()
                .flatMap(it -> it.getLineStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .collect(Collectors.toList());

        IntStream.range(0, path.size() - 1)
                .forEach(i -> {
                    lineStations.stream()
                            .filter(ls -> Objects.equals(ls.getPreStationId(), path.get(i)) && Objects.equals(ls.getStationId(), path.get(i + 1)))
                            .findFirst()
                            .ifPresent(pathLineStations::add);
                });
        return pathLineStations;
    }
}
