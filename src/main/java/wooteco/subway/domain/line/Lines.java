package wooteco.subway.domain.line;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.path.PathType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Lines {
    private List<Line> lines;

    public Lines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Long> getStationIds() {
        return lines.stream()
                .flatMap(it -> it.getStations().stream())
                .map(LineStation::getStationId)
                .collect(Collectors.toList());
    }

    public WeightedMultigraph<Long, DefaultWeightedEdge> addVertexAndEdges(final WeightedMultigraph<Long, DefaultWeightedEdge> graph, PathType type) {
        lines.stream()
                .flatMap(it -> it.getStationIds().stream())
                .forEach(graph::addVertex);

        lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .forEach(it -> graph.setEdgeWeight(graph.addEdge(it.getPreStationId(), it.getStationId()), type.findWeightOf(it)));

        return graph;
    }

    public LineStations findLineStations() {
        return new LineStations(lines.stream()
                .flatMap(it -> it.getStations().stream())
                .filter(it -> Objects.nonNull(it.getPreStationId()))
                .collect(Collectors.toList()));
    }
}
