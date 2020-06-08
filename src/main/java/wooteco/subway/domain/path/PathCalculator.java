package wooteco.subway.domain.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.LineStations;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.service.path.NotExistedPathException;

import java.util.ArrayList;
import java.util.List;

public class PathCalculator {

    public static List<Long> findPath(final Lines lines, final Long sourceStationId,
                                      final Long targetStationId, final PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        graph = lines.addVertexAndEdges(graph, type);

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            return dijkstraShortestPath.getPath(sourceStationId, targetStationId).getVertexList();
        } catch(IllegalArgumentException e) {
            throw new NotExistedPathException();
        }
    }

    public static LineStations extractPathLineStation(List<Long> path, LineStations lineStations) {
        Long preStationId = path.get(0);
        List<LineStation> paths = new ArrayList<>();

        for (int index = 1; index < path.size(); index ++) {
            Long finalPreStationId = preStationId;
            LineStation lineStation = lineStations.findLineStation(path.get(index), finalPreStationId);

            paths.add(lineStation);
            preStationId = path.get(index);
        }

        return new LineStations(paths);
    }
}
