package wooteco.subway.service.member.dto;

import wooteco.subway.domain.member.favorite.Favorite;

import java.util.Map;

public class FavoriteResponse {
    private Long sourceStationId;
    private String sourceStationName;
    private Long targetStationId;
    private String targetStationName;

    public FavoriteResponse(final Long sourceStationId, final String sourceStationName, final Long targetStationId, final String targetStationName) {
        this.sourceStationId = sourceStationId;
        this.sourceStationName = sourceStationName;
        this.targetStationId = targetStationId;
        this.targetStationName = targetStationName;
    }

    public FavoriteResponse(final Favorite favorite, final Map<Long, String> stations) {
        this(favorite.getSourceStationId(), stations.get(favorite.getSourceStationId()), favorite.getTargetStationId(), stations.get(favorite.getTargetStationId()));
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public String getTargetStationName() {
        return targetStationName;
    }
}
