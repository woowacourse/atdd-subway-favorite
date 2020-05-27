package wooteco.subway.service.member.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.favorite.FavoriteDetail;

public class FavoriteResponse {
    private Long sourceId;
    private Long targetId;
    private String sourceName;
    private String targetName;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long sourceId, Long targetId, String sourceName,
        String targetName) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sourceName = sourceName;
        this.targetName = targetName;
    }

    public FavoriteResponse(FavoriteDetail favoriteDetail) {
        this(favoriteDetail.getSourceId(), favoriteDetail.getTargetId(),
            favoriteDetail.getSourceName(), favoriteDetail.getTargetName());
    }

    public static List<FavoriteResponse> of(List<FavoriteDetail> favorites) {
        return favorites.stream()
            .map(FavoriteResponse::new)
            .collect(Collectors.toList());
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTargetName() {
        return targetName;
    }
}
