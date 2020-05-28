package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.station.StationService;

import java.util.List;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationService stationService;

    public FavoriteService(MemberRepository memberRepository, StationService stationService) {
        this.memberRepository = memberRepository;
        this.stationService = stationService;
    }

    public void createFavorite(FavoriteRequest favoriteRequest, Member member) {
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.addFavorite(new Favorite(sourceId, destinationId));
        memberRepository.save(member);
    }

    public void removeFavorite(FavoriteRequest favoriteRequest, Member member) {
        Long sourceId = stationService.findStationByName(favoriteRequest.getSourceName()).getId();
        Long destinationId = stationService.findStationByName(favoriteRequest.getDestinationName()).getId();
        member.removeFavoriteById(sourceId, destinationId);
        memberRepository.save(member);
    }

    public List<FavoriteResponse> showAllFavorites(Member member) {
        Favorites favorites = new Favorites(member.getFavorites());

        return favorites.toFavoriteResponses(stationService.findStations());
    }

    public boolean ifFavoriteExist(Member member, String source, String destination) {
        Favorites favorites = new Favorites(member.getFavorites());
        Long sourceId = stationService.findStationByName(source).getId();
        Long destinationId = stationService.findStationByName(destination).getId();
        return favorites.findById(sourceId, destinationId).isPresent();
    }
}
