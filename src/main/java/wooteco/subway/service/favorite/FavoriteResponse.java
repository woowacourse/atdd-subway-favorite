package wooteco.subway.service.favorite;

import wooteco.subway.service.station.dto.StationResponse;

public class FavoriteResponse {
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(StationResponse source,
        StationResponse target) {
        this.source = source;
        this.target = target;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
