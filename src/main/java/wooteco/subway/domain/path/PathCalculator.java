package wooteco.subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.service.path.NotExistedPathException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathCalculator {

    public PathCalculator() {
    }

    public List<Long> findPath(final List<Line> lines, final Long sourceStationId,
                               final Long targetStationId, final PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.stream()
                .flatMap(it -> it.getStationIds().stream())
                .forEach(it -> graph.addVertex(it));

        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getPreStationId(), it.getStationId()), type.findWeightOf(it)));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return dijkstraShortestPath.getPath(sourceStationId, targetStationId).getVertexList();
        } catch(IllegalArgumentException e) {
            throw new NotExistedPathException();
        }
    }

    public List<LineStation> extractPathLineStation(List<Long> path, List<LineStation> lineStations) {
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
