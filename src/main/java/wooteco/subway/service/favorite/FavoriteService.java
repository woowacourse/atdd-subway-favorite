package wooteco.subway.service.favorite;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

@Service
public class FavoriteService {

    public void delete(Long memberId, FavoriteRequest favoriteRequest) {}

    public Long create(Long memberId, FavoriteRequest favoriteRequest) {
        return null;
    }

    public List<FavoriteResponse> findAll(Long memberId) {
        return null;
    }
}
