package wooteco.subway.service.member.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class FavoriteRequest {
    @JsonAlias("source")
    private Long sourceStationId;
    @JsonAlias("target")
    private Long targetStationId;

    private FavoriteRequest() {
    }

    public FavoriteRequest(final Long sourceStationId, final Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
