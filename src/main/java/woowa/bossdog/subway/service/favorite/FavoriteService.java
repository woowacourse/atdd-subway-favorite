package woowa.bossdog.subway.service.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowa.bossdog.subway.domain.Favorite;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.repository.FavoriteRepository;
import woowa.bossdog.subway.repository.StationRepository;
import woowa.bossdog.subway.service.favorite.dto.FavoriteRequest;
import woowa.bossdog.subway.service.favorite.dto.FavoriteResponse;
import woowa.bossdog.subway.web.station.NotExistedStationException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    @Transactional
    public FavoriteResponse createFavorite(final Member member, final FavoriteRequest request) {
        Station sourceStation = stationRepository.findById(request.getSource())
                .orElseThrow(NotExistedStationException::new);
        Station targetStation = stationRepository.findById(request.getTarget())
                .orElseThrow(NotExistedStationException::new);

        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> listFavorites(final Member member) {
        final List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return FavoriteResponse.listFrom(favorites);
    }

    @Transactional
    public void deleteFavorite(final Member member, final Long id) {
        favoriteRepository.deleteById(id);
    }
}
