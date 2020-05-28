package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository,
                           StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void create(Member member, FavoriteCreateRequest favoriteCreateRequest) {
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        Long source = stationRepository.findIdByName(favoriteCreateRequest.getSource())
                .orElseThrow(() -> new IllegalArgumentException("시작역을 찾을 수 없습니다."));
        Long target = stationRepository.findIdByName(favoriteCreateRequest.getTarget())
                .orElseThrow(() -> new IllegalArgumentException("도착역을 찾을 수 없습니다."));
        Favorite favorite = new Favorite(source, target);

        persistMember.addFavorite(favorite);

        memberRepository.save(persistMember);

    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> find(Member member) {
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));
        Set<Favorite> favorites = persistMember.getFavorites();

        return favorites.stream()
                .map(this::toFavoriteResponse)
                .collect(Collectors.toList());
    }

    private FavoriteResponse toFavoriteResponse(Favorite favorite) {
        return new FavoriteResponse(
                stationRepository.findNameById(favorite.getSource())
                        .orElseThrow(() -> new IllegalArgumentException("시작역을 찾을 수 없습니다.")),
                stationRepository.findNameById(favorite.getTarget())
                        .orElseThrow(() -> new IllegalArgumentException("도착역을 찾을 수 없습니다.")));
    }

    @Transactional
    public void delete(Member member, FavoriteDeleteRequest favoriteDeleteRequest) {
        Favorite favorite = toFavorite(favoriteDeleteRequest);
        Member persistMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("멤버를 찾을 수 없습니다."));

        persistMember.deleteFavorite(favorite);

        memberRepository.save(persistMember);
    }

    private Favorite toFavorite(FavoriteDeleteRequest favoriteDeleteRequest) {
        return new Favorite(
                stationRepository.findIdByName(favoriteDeleteRequest.getSource())
                        .orElseThrow(() -> new IllegalArgumentException("시작역을 찾을 수 없습니다.")),
                stationRepository.findIdByName(favoriteDeleteRequest.getTarget())
                        .orElseThrow(() -> new IllegalArgumentException("도착역을 찾을 수 없습니다.")));
    }
}
