package wooteco.subway.service.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exceptions.NotExistFavoritePathException;
import wooteco.subway.exceptions.NotExistStationException;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

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
            throw new NotExistFavoritePathException();
        }

        Lines lines = new Lines(lineRepository.findAll());
        Station sourceStation = stationRepository.findByName(source)
            .orElseThrow(() -> new NotExistStationException(source));
        Station targetStation = stationRepository.findByName(target)
            .orElseThrow(() -> new NotExistStationException(target));

        List<Long> path = graphService.findPath(lines, sourceStation.getId(), targetStation.getId(), type);
        List<Station> stations = stationRepository.findAllById(path);

        LineStations paths = new LineStations(extractPathLineStation(path, lines.getLineStations()));
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
            .orElseThrow(() -> new NotExistStationException(stationId));
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
