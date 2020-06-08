package wooteco.subway.service.path;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.PathCalculator;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.StationService;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;

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

        Lines lines = lineService.findLines();
        Station sourceStation = stationService.findByName(source);
        Station targetStation = stationService.findByName(target);

        List<Long> path = PathCalculator.findPath(lines, sourceStation.getId(), targetStation.getId(), type);

        LineStations paths = PathCalculator.extractPathLineStation(path, lines.findLineStations());
        int duration = paths.getTotalDuration();
        int distance = paths.getTotalDistance();

        Stations pathStation = paths.findStations(path, stationService.findAllById(path));

        return new PathResponse(StationResponse.listFrom(pathStation), duration, distance);
    }
}