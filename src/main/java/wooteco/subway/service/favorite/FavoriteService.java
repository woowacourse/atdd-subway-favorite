package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;
import wooteco.subway.web.member.exception.NotExistStationDataException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Member member, CreateFavoriteRequest createFavoriteRequest) {
        Station sourceStation = stationRepository.findByName(createFavoriteRequest.getSource())
                .orElseThrow(() -> new NotExistStationDataException("source station name = " + createFavoriteRequest.getSource()));
        Station targetStation = stationRepository.findByName(createFavoriteRequest.getTarget())
                .orElseThrow(() -> new NotExistStationDataException("target station name = " + createFavoriteRequest.getTarget()));

        member.addFavorite(new Favorite(sourceStation.getId(), targetStation.getId()));

        Member savedMember = memberRepository.save(member);

        Favorite savedFavorite = savedMember.findFavoriteBySourceAndTarget(sourceStation, targetStation);

        return new FavoriteResponse(savedFavorite.getId(), sourceStation.getName(), targetStation.getName());
    }

    @Transactional(readOnly = true)
    public FavoritesResponse findAllByMemberId(Member member) {
        Favorites favorites = member.getFavorites();

        List<Long> favoriteSourceIds = favorites.extractSourceIds();
        List<Long> favoriteTargetIds = favorites.extractTargetIds();

        Stations sourceStations = new Stations(stationRepository.findAllById(favoriteSourceIds));
        Stations targetStations = new Stations(stationRepository.findAllById(favoriteTargetIds));

        return FavoritesResponse.of(favorites, sourceStations, targetStations);
    }

    @Transactional
    public void deleteFavorite(Member member, Long id) throws AccessDeniedException {
        try {
            member.removeFavoriteById(id);
        } catch (NotExistFavoriteDataException e) {
            throw new AccessDeniedException("요청을 수행할 수 있는 권한이 없습니다.");
        }

        memberRepository.save(member);
    }
}
