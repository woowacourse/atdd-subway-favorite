package wooteco.subway.service.favorite;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void createFavorite(Long memberId, FavoriteCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
        Long sourceStationId = stationRepository.findIdByName(request.getSourceStationName());
        Long targetStationId = stationRepository.findIdByName(request.getTargetStationName());
        Favorite favorite = new Favorite(sourceStationId, targetStationId);
        member.addFavorite(favorite);
    }
}
