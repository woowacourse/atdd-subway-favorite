package wooteco.subway.service.favorite.dto;

import wooteco.subway.domain.favorite.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private Long sourceStationId;
    private Long targetStationId;

    private FavoriteResponse() {
    }

    public FavoriteResponse(final Long id, final Long memberId, final Long sourceStationId, final Long targetStationId) {
        this.id = id;
        this.memberId = memberId;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public static FavoriteResponse from(final Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMemberId(),
                favorite.getSourceStationId(), favorite.getTargetStationId());
    }

    public static List<FavoriteResponse> listFrom(final List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
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
}
