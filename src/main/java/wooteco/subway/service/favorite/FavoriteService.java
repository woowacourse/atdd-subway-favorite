package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.Collections;
import java.util.List;

@Service
public class FavoriteService {
    public void create(Member member, FavoriteCreateRequest favoriteCreateRequest) {
    }

    public List<FavoriteResponse> find(Member member) {
        return Collections.emptyList();
    }

    public void delete(Member member, FavoriteDeleteRequest favoriteDeleteRequest) {
    }
}
