package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;
import wooteco.subway.web.member.exception.NotExistStationDataException;
import wooteco.subway.web.member.exception.UnAuthorizationException;

import java.util.List;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Member member, CreateFavoriteRequest createFavoriteRequest) {
        Station sourceStation = stationRepository.findByName(createFavoriteRequest.getSource())
                .orElseThrow(() -> new NotExistStationDataException("source station name = " + createFavoriteRequest.getSource()));

        Station targetStation = stationRepository.findByName(createFavoriteRequest.getTarget())
                .orElseThrow(() -> new NotExistStationDataException("target station name = " + createFavoriteRequest.getTarget()));

        Favorite savedFavorite = favoriteRepository.save(new Favorite(member.getId(), sourceStation.getId(), targetStation.getId()));

        return new FavoriteResponse(savedFavorite.getId(), sourceStation.getName(), targetStation.getName());
    }

    public Favorite findFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new NotExistFavoriteDataException("id : " + id));
    }

    public FavoritesResponse findAllByMemberId(Long memberId) {
        Favorites favorites = new Favorites(favoriteRepository.findAllByMemberId(memberId));

        List<Long> favoriteSourceIds = favorites.extractSourceIds();
        List<Long> favoriteTargetIds = favorites.extractTargetIds();

        List<Station> sourceStations = stationRepository.findAllById(favoriteSourceIds);
        List<Station> targetStations = stationRepository.findAllById(favoriteTargetIds);

        return FavoritesResponse.of(favorites, sourceStations, targetStations);
    }

    public void deleteFavorite(Member member, Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new NotExistFavoriteDataException("ID = " + id));
        System.out.println(favorite + "fa");
        System.out.println(member + "me");

        if (favorite.isNotEqualToMemberId(member.getId())) {
            System.out.println("여기서 터지는것? ");
            throw new UnAuthorizationException();
        }
        favoriteRepository.deleteById(id);
    }
}
