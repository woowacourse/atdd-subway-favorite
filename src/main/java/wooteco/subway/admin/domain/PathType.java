package wooteco.subway.admin.domain;

import java.util.function.Function;

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
