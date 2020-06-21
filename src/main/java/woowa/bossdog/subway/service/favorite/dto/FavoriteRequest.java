package woowa.bossdog.subway.service.favorite.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }
}
