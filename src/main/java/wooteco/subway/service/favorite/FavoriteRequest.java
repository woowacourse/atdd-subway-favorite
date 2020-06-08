package wooteco.subway.service.favorite;

import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public FavoriteStation toFavoriteStation(Member member) {
        return new FavoriteStation(member.getId(), source, target);
    }
}
