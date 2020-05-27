package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Favorites;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.exception.WrongStationException;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

import java.util.List;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void createFavorite(FavoriteRequest favoriteRequest, Member member) {
        Long sourceId = findStationByName(favoriteRequest.getSourceName());
        Long destinationId = findStationByName(favoriteRequest.getDestinationName());
        member.addFavorite(new Favorite(sourceId, destinationId));
        memberRepository.save(member);
    }

    public void removeFavorite(FavoriteRequest favoriteRequest, Member member) {
        Long sourceId = findStationByName(favoriteRequest.getSourceName());
        Long destinationId = findStationByName(favoriteRequest.getDestinationName());
        member.removeFavoriteById(sourceId, destinationId);
        memberRepository.save(member);
    }

    public List<FavoriteResponse> showAllFavorites(Member member) {
        Favorites favorites = new Favorites(member.getFavorites());

        return favorites.toFavoriteResponses(stationRepository.findAll());
    }

    public boolean ifFavoriteExist(Member member, String source, String destination) {
        Favorites favorites = new Favorites(member.getFavorites());
        Long sourceId = findStationByName(source);
        Long destinationId = findStationByName(destination);
        return favorites.findById(sourceId, destinationId).isPresent();
    }

    private Long findStationByName(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(WrongStationException::new)
                .getId();
    }
}
