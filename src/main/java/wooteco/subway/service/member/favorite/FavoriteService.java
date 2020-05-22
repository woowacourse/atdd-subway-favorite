package wooteco.subway.service.member.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.domain.station.Station;

@Service
public class FavoriteService {
    public void addFavoriteToMember(Member member, Station source, Station target) {
        Favorite favorite = Favorite.of(source, target);
        member.addFavorite(favorite);
    }
}
