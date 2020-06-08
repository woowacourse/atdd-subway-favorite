package wooteco.subway.service.path;

import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import wooteco.subway.domain.line.LineStation;
import wooteco.subway.domain.line.Lines;
import wooteco.subway.domain.path.PathType;

@Service
public class GraphService {
    public List<Long> findPath(Lines lines, Long source, Long target, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Long stationId : lines.getStationIds()) {
            graph.addVertex(stationId);
        }

        for (LineStation lineStation : lines.getLineStations()) {
            graph.setEdgeWeight(graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()),
                type.findWeightOf(lineStation));
        }

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }
}
