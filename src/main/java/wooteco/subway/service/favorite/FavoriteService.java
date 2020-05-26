package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new NotExistFavoriteDataException(source + "," + target));
    }

    public Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new NotExistFavoriteDataException("id : " + id));
    }


    public Favorite save(CreateFavoriteRequest request, String email) {
        Favorite favorite = new Favorite(request.getSource(), request.getTarget(), email);
        return favoriteRepository.save(favorite);
    }

    public List<FavoriteResponse> findAllByEmail(String email) {
        return favoriteRepository.findByEmail(email).stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Favorite favorite) {
        System.out.println(favorite.getId() + "아이디이");
        favoriteRepository.deleteById(favorite.getId());
    }
}
