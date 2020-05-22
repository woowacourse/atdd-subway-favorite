package wooteco.subway.domain.path;

import wooteco.subway.domain.line.LineStation;

import java.util.function.Function;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private final Function<LineStation, Integer> expression;

    PathType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }


    public int findWeightOf(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}
