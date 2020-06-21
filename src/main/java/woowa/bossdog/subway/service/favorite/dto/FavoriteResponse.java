package woowa.bossdog.subway.service.favorite.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import woowa.bossdog.subway.domain.Favorite;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(final Long id, final Long memberId, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(final Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMember().getId(),
                StationResponse.from(favorite.getSourceStation()), StationResponse.from(favorite.getTargetStation()));
    }

    public static List<FavoriteResponse> listFrom(final List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
