package wooteco.subway.domain.path;

import java.util.List;

import org.jgrapht.GraphPath;

public class Path {

    private final GraphPath<Long, Edge> path;

    private Path(GraphPath<Long, Edge> path) {
        this.path = path;
    }

    public static Path of(GraphPath<Long, Edge> path) {
        return new Path(path);
    }

    public List<Long> getVertexList() {
        return path.getVertexList();
    }

    public List<Edge> getEdgeList() {
        return path.getEdgeList();
    }
}
