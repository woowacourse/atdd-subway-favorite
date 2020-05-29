package wooteco.subway.service.path;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Edge;
import wooteco.subway.domain.path.Graph;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

@Component
public class Graphs {

    private ConcurrentMap<PathType, Graph> graphs = new ConcurrentHashMap<>();
    private ConcurrentMap<Long, Station> stationMatcher = new ConcurrentHashMap<>();

    public void initialize(List<Line> lines, List<Station> stations) {
        stationMatcher = stations
            .stream()
            .collect(Collectors.toConcurrentMap(Station::getId, station -> station));
        for (PathType pathType : PathType.values()) {
            graphs.put(pathType, Graph.of(lines, stationMatcher, pathType));
        }
    }

    public PathResponse findPath(Long source, Long target, PathType pathType) {
        Graph graph = graphs.get(pathType);
        Path path = graph.getPath(source, target);
        List<Long> stationIds = getStations(path);
        List<Edge> edges = path.getEdgeList();
        List<StationResponse> responses = getStationResponses(stationIds);
        int distance = getDistance(edges);
        int duration = getDuration(edges);
        return PathResponse.of(responses, duration, distance);
    }

    private List<Long> getStations(Path path) {
        try {
            return path.getVertexList();
        } catch (IllegalArgumentException ie) {
            throw new IllegalArgumentException("등록되지 않은 역이 포함되어 있습니다.");
        } catch (NullPointerException ne) {
            throw new IllegalArgumentException("두 역은 연결되지 않아 갈 수 없습니다.");
        }
    }

    private int getDuration(List<Edge> edges) {
        return edges.stream()
            .mapToInt(Edge::getDuration)
            .sum();
    }

    private int getDistance(List<Edge> edges) {
        return edges.stream()
            .mapToInt(Edge::getDistance)
            .sum();
    }

    private List<StationResponse> getStationResponses(List<Long> stationIds) {
        return stationIds.stream()
            .map(stationMatcher::get)
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

}
