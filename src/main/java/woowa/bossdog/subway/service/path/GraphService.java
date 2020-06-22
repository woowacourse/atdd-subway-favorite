package woowa.bossdog.subway.service.path;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import woowa.bossdog.subway.domain.Line;
import woowa.bossdog.subway.service.path.dto.PathType;
import woowa.bossdog.subway.web.path.NotExistedPathException;

import java.util.List;
import java.util.Objects;

@Service
public class GraphService {

    public List<Long> findPath(List<Line> lines, Long source, Long target, PathType type) {
        WeightedMultigraph<Long, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);

        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .forEach(it -> graph.addVertex(it.getId()));

        lines.stream()
                .flatMap(it -> it.getLineStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStation()))
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getPreStation().getId(),
                        it.getStation().getId()), type.findWeightOf(it)));
        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            return dijkstraShortestPath.getPath(source, target).getVertexList();
        } catch (IllegalArgumentException e) {
            throw new NotExistedPathException();
        }
    }
}
