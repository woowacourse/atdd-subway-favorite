package wooteco.subway.service.favorite.dto;

import javax.validation.constraints.NotNull;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.web.exception.validator.CheckFavoriteSameStation;

@CheckFavoriteSameStation
public class FavoriteRequest {

    @NotNull(message = "즐겨찾기에 source를 입력해주세요!")
    private Long source;
    @NotNull(message = "즐겨찾기에 target를 입력해주세요!")
    private Long target;

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Favorite toFavorite(Long memberId) {
        return Favorite.of(memberId, source, target);
    }
}
