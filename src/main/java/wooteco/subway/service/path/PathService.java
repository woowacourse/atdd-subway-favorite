package wooteco.subway.service.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.NoStationExistsException;
import wooteco.subway.exception.SourceEqualsTargetException;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;

@Service
public class PathService {
	private final StationRepository stationRepository;
	private final LineRepository lineRepository;

	public PathService(StationRepository stationRepository, LineRepository lineRepository) {
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
	}

	public PathResponse findPath(String source, String target, PathType type) {
		validateSourceEqualsTarget(source, target);

		Path path = new Path(new WeightedMultigraph(DefaultWeightedEdge.class));

		Lines lines = Lines.of(lineRepository.findAll());
		List<Long> shortestPath = findShortestPath(source, target, type, path, lines);

		LineStations paths = path.extractPathLineStations(lines.toLineStations());

		Stations stations = Stations.of(stationRepository.findAllById(shortestPath));
		List<Station> pathStation = path.extractPathStation(stations);
		int duration = paths.extractShortestDistance();
		int distance = paths.extractShortestDuration();

		return new PathResponse(StationResponse.listOf(pathStation), duration, distance);
	}

	private void validateSourceEqualsTarget(String source, String target) {
		if (Objects.equals(source, target)) {
			throw new SourceEqualsTargetException();
		}
	}

	private List<Long> findShortestPath(String source, String target, PathType type, Path path, Lines lines) {
		Station sourceStation = findStationByNameOf(source);
		Station targetStation = findStationByNameOf(target);
		return path.findShortestPath(lines, sourceStation.getId(), targetStation.getId(), type);
	}

	private Station findStationByNameOf(String target) {
		return stationRepository.findByName(target)
				.orElseThrow(NoStationExistsException::new);
	}
}
