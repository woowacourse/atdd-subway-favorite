package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.service.exception.SameStationException;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.station.StationService;

import java.util.List;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationService stationService;

    public FavoriteService(MemberRepository memberRepository, StationService stationService) {
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    @Transactional
    public void createFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.addFavorite(new Favorite(sourceId, destinationId));
        memberRepository.save(member);
    }

    @Transactional
    public void removeFavorite(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.removeFavoriteById(sourceId, destinationId);
        memberRepository.save(member);
    }

    public List<FavoriteResponse> showAllFavorites(Member member) {
        Favorites favorites = new Favorites(member.getFavorites());

        return FavoriteResponse.toFavoriteResponses(new Stations(stationService.findStations()), favorites);
    }

    public boolean ifFavoriteExist(FavoriteRequest favoriteRequest, Member member) {
        validate(favoriteRequest);
        Favorites favorites = new Favorites(member.getFavorites());
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        return favorites.existsFavorite(sourceId, destinationId);
    }

    private void validate(FavoriteRequest favoriteRequest) {
        if (favoriteRequest.getSourceName().equals(favoriteRequest.getDestinationName())) {
            throw new SameStationException();
        }
    }
}
