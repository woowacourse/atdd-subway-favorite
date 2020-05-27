package wooteco.subway.domain.path;

import java.util.function.Function;
import wooteco.subway.domain.line.LineStation;

public enum PathType {
    DISTANCE(lineStation -> lineStation.getDistance()),
    DURATION(lineStation -> lineStation.getDuration());

    private Function<LineStation, Integer> expression;

    PathType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }


    public int findWeightOf(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}
