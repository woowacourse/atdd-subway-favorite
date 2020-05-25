package wooteco.subway.service.favorite;

import wooteco.subway.domain.station.Station;

public class FavoriteResponse {
    private final String source;
    private final String target;

    public FavoriteResponse(Station source, Station target) {
        this.source = source.getName();
        this.target = target.getName();
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }
}
