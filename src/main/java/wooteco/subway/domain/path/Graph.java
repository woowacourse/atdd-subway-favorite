package wooteco.subway.domain.path;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.station.Station;

public class Graph {

    private final WeightedMultigraph<Long, Edge> graph;

    public Graph(WeightedMultigraph<Long, Edge> graph) {
        this.graph = graph;
    }

    public static Graph of(List<Line> lines, Map<Long, Station> stationMatcher, PathType pathType) {
        WeightedMultigraph<Long, Edge> graph = new WeightedMultigraph<>(
            Edge.class);
        addStationsAsVertex(stationMatcher, graph);
        addEdge(lines, pathType, graph);
        return new Graph(graph);
    }

    private static void addStationsAsVertex(Map<Long, Station> stationMatcher,
        WeightedMultigraph<Long, Edge> graph) {
        for (Station station : stationMatcher.values()) {
            graph.addVertex(station.getId());
        }
    }

    private static void addEdge(List<Line> lines, PathType pathType,
        WeightedMultigraph<Long, Edge> graph) {
        for (Line line : lines) {
            addEdgeByLine(pathType, graph, line);
        }
    }

    private static void addEdgeByLine(PathType pathType,
        WeightedMultigraph<Long, Edge> graph, Line line) {
        for (LineStation lineStation : line.getLineStations()) {
            if (Objects.isNull(lineStation.getPreStationId())) {
                continue;
            }
            addEdge(pathType, graph, lineStation);
        }
    }

    private static void addEdge(PathType pathType, WeightedMultigraph<Long, Edge> graph,
        LineStation lineStation) {
        Edge edge = Edge.of(lineStation);
        graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId(),
            edge);
        graph.setEdgeWeight(edge, pathType.getWeight(lineStation));
    }

    public Path getPath(Long source, Long target) {
        DijkstraShortestPath<Long, Edge> dijkstraShortestPath = new DijkstraShortestPath<>(
            graph);
        GraphPath<Long, Edge> path = dijkstraShortestPath.getPath(source, target);
        return Path.of(path);
    }
}
