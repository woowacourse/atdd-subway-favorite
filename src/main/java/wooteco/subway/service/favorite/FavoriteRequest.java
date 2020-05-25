package wooteco.subway.service.favorite;

import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;

public class FavoriteRequest {

    private String source;
    private String target;

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public FavoriteStation toFavoriteStation(Member member) {
        return new FavoriteStation(member.getId(), source, target);
    }
}
