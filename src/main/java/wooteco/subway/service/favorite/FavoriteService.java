package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite createFavorite(String source, String target, String memberEmail) {
        return favoriteRepository.save(new Favorite(source, target, memberEmail));
    }

    public Favorite findFavoriteBySourceAndTarget(String source, String target) {
        return favoriteRepository.findBySourceAndTarget(source, target)
                .orElseThrow(() -> new NotExistFavoriteDataException(source + "," +  target));
    }

    public Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }
}
