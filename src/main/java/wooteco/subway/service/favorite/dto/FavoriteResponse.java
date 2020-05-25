package wooteco.subway.service.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.Favorite;

public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;
    private String sourceStationName;
    private String targetStationName;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long memberId, Long sourceStationId,
        Long targetStationId, String sourceStationName, String targetStationName) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.sourceStationName = sourceStationName;
        this.targetStationName = targetStationName;
    }

    public static FavoriteResponse of(Favorite favorite, String sourceName, String targetName) {
        return new FavoriteResponse(favorite.getId(), favorite.getMemberId(),
            favorite.getSourceStationId(), favorite.getTargetStationId(), sourceName, targetName);
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMemberId(),
            favorite.getSourceStationId(), favorite.getTargetStationId(), null, null);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public String getSourceStationName() {
        return sourceStationName;
    }

    public String getTargetStationName() {
        return targetStationName;
    }
}
