package wooteco.subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;

import wooteco.subway.domain.line.LineStation;

public class Edge extends DefaultWeightedEdge {
    private int distance;
    private int duration;

    private Edge(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    public static Edge of(LineStation lineStation) {
        return new Edge(lineStation.getDistance(), lineStation.getDuration());
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
