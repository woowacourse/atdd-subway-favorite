package wooteco.subway.domain.path;

import wooteco.subway.domain.line.LineStation;

import java.util.function.Function;

public enum PathType {
    DISTANCE(lineStation -> lineStation.getDistance()),
    DURATION(lineStation -> lineStation.getDuration());

    private final Function<LineStation, Integer> expression;

    PathType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }

    public int findWeightOf(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}
