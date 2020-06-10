package wooteco.subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.NoPathExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Path {
	private WeightedMultigraph<Long, DefaultWeightedEdge> graph;
	private List<Long> shortestPath;

	public Path(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public List<Long> findShortestPath(Lines lines, Long source, Long target, PathType type) {
		try {
			lines.addVertex(graph);
			lines.setEdge(graph, type);

			DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
			shortestPath = dijkstraShortestPath.getPath(source, target).getVertexList();

		} catch (NullPointerException e) {
			throw new NoPathExistsException();
		}

		return shortestPath;
	}

	public LineStations extractPathLineStations(LineStations lineStations) {
		Long preStationId = null;
		List<LineStation> paths = new ArrayList<>();

		for (Long stationId : shortestPath) {
			preStationId = addPath(lineStations, preStationId, paths, stationId);
		}

		return LineStations.of(paths);
	}

	public List<Station> extractPathStation(Stations stations) {
		return shortestPath.stream()
				.map(stations::extractStationById)
				.collect(Collectors.toList());
	}

	private Long addPath(LineStations lineStations, Long preStationId, List<LineStation> paths, Long stationId) {
		if (preStationId == null) {
			preStationId = stationId;
			return preStationId;
		}

		Long finalPreStationId = preStationId;
		LineStation foundLineStation = lineStations.findLineStation(finalPreStationId, stationId);

		paths.add(foundLineStation);
		preStationId = stationId;
		return preStationId;
	}
}
