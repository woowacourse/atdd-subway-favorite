package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse addFavorite(final Long memberId, final FavoriteRequest request) {
        Favorite favorite = favoriteRepository.save(request.toFavorite(memberId));

        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> showMyAllFavorites(final Long memberId) {
        return FavoriteResponse.listFrom(favoriteRepository.findAllByMemberId(memberId));
    }

    public void removeFavorite(final Long id) {
        favoriteRepository.deleteById(id);
    }
}
