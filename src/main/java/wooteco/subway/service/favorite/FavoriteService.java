package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    public FavoriteResponse createFavorite(Member member, FavoriteRequest request) {
        return null;
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        return new ArrayList<>();
    }

    public void deleteFavorite(Member member, Long id) {

    }
}
