package wooteco.subway.service.member;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@Service
public class FavoriteService {

    private static final String DELETED_STATION = "삭제된 역";

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository,
        StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void createFavorite(Member member, FavoriteRequest request) {
        Favorite favorite = request.toFavorite();
        member.addFavorite(favorite);
        memberRepository.save(member);
    }

    public List<FavoriteResponse> findFavorites(Member member) {
        return member.getFavorites()
            .stream()
            .map(favorite -> findFavorite(member, favorite))
            .collect(Collectors.toList());
    }

    private FavoriteResponse findFavorite(Member member, Favorite favorite) {
        String source = stationRepository.findById(favorite.getSourceId())
            .orElseGet(() -> new Station(DELETED_STATION))
            .getName();
        String target = stationRepository.findById(favorite.getTargetId())
            .orElseGet(() -> new Station(DELETED_STATION))
            .getName();

        if(isDeleted(source, target)) {
            member.removeFavorite(favorite);
            memberRepository.save(member);
        }
        return new FavoriteResponse(source, target);
    }

    private boolean isDeleted(String source, String target) {
        return DELETED_STATION.equals(source) || DELETED_STATION.equals(target);
    }

    public void deleteFavorite(Member member, FavoriteDeleteRequest request) {
        if(isDeleted(request.getSourceName(), request.getTargetName())) {
            return;
        }

        Favorite favorite = findFavoriteByName(request.getSourceName(), request.getTargetName());
        member.removeFavorite(favorite);
        memberRepository.save(member);
    }

    private Favorite findFavoriteByName(String sourceName, String targetName) {
        Long sourceId = stationRepository.findByName(sourceName)
            .orElseThrow(NoSuchElementException::new)
            .getId();
        Long targetId = stationRepository.findByName(targetName)
            .orElseThrow(NoSuchElementException::new)
            .getId();
        return new Favorite(sourceId, targetId);
    }
}
