package wooteco.subway.acceptance.favorite.dto;

import wooteco.subway.service.station.dto.StationResponse;

public class StationPathResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public StationPathResponse() {
    }

    public StationPathResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
