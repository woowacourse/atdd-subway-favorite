package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteRequest;

import java.util.List;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public void createFavorite(Member member, FavoriteRequest view) {
        Favorite favorite = new Favorite(member.getId(), view.getSource(), view.getTarget());
        favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Member member, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
        favorite.checkMember(member);
        favoriteRepository.deleteById(id);
    }

    public List<Favorite> findFavoritesByMember(Member member) {
        return favoriteRepository.findByMemberId(member.getId());
    }
}
