package wooteco.subway.domain.path;

import java.util.function.Function;

import wooteco.subway.domain.line.LineStation;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> function;

    public static PathType of(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ie) {
            return DISTANCE;
        }
    }

    PathType(Function<LineStation, Integer> function) {
        this.function = function;
    }

    public int getWeight(LineStation lineStation) {
        return function.apply(lineStation);
    }
}
