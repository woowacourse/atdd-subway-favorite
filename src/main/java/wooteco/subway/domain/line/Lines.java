package wooteco.subway.domain.line;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.path.PathType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Lines {
    private final List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public void addVertex(WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        lines.stream()
                .flatMap(line -> line.getStationIds().stream())
                .forEach(graph::addVertex);
    }

    public void setEdge(WeightedMultigraph<Long, DefaultWeightedEdge> graph, PathType type) {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .forEach(lineStation -> graph.setEdgeWeight(
                        graph.addEdge(lineStation.getPreStationId(), lineStation.getStationId()),
                        type.findWeightOf(lineStation)
                ));
    }

    public LineStations toLineStations() {
        return LineStations.of(lines.stream()
                .flatMap(line -> line.getStations().stream())
                .filter(lineStation -> Objects.nonNull(lineStation.getPreStationId()))
                .collect(Collectors.toList()));
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<Long> getStationIds() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .map(LineStation::getStationId)
                .collect(Collectors.toList());
    }
}
