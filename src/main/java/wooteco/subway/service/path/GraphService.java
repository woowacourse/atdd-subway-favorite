package wooteco.subway.service.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.PathType;

import java.util.List;
import java.util.Objects;

@Service
public class GraphService {
    public List<Long> findPath(List<Line> lines, Long source, Long target, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.stream()
                .flatMap(line -> line.getStationIds().stream())
                .forEach(graph::addVertex);

        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .forEach(lineStation -> graph.setEdgeWeight(
                        graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()),
                        type.findWeightOf(lineStation)
                ));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
