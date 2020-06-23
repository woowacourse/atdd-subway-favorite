package woowa.bossdog.subway.service.path.dto;

import woowa.bossdog.subway.domain.LineStation;

import java.util.function.Function;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private final Function<LineStation, Integer> expression;

    PathType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }

    public double findWeightOf(final LineStation lineStation) {
        return expression.apply(lineStation);
    }
}
