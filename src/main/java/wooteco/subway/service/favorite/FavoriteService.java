package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.favorite.FavoriteStation;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteStation save(FavoriteStation favoriteStation) {
        return favoriteRepository.save(favoriteStation);
    }
}
