package wooteco.subway.service.favorite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;

    public FavoriteService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void save(Member member, FavoriteStation favoriteStation) {
        if (member.contain(favoriteStation)) {
            throw new IllegalArgumentException();
        }
        member.addFavoriteStation(favoriteStation);
        memberRepository.save(member);
    }

    public FavoritesResponse findAll(Member member) {
        List<FavoriteStation> favoriteStations = new ArrayList<>(member.getFavoriteStations());
        return FavoritesResponse.of(favoriteStations);
    }

    public void delete(Member member, String source, String target) {
        FavoriteStation favoriteStation = member.findByNames(source, target);
        member.deleteFavoriteStation(favoriteStation);
        memberRepository.save(member);
    }
}