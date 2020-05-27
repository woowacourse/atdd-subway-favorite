package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.favorite.FavoriteRepository;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteReadRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.station.NoSuchStationException;
import wooteco.subway.web.member.DuplicateMemberException;
import wooteco.subway.web.member.InvalidAuthenticationException;
import wooteco.subway.web.member.NoSuchFavoriteException;
import wooteco.subway.web.member.NoSuchMemberException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new DuplicateMemberException();
        }
        return memberRepository.save(member);
    }

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        if (!memberRepository.findById(id).isPresent()) {
            throw new NoSuchMemberException();
        }
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(RuntimeException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new InvalidAuthenticationException("잘못된 패스워드");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public List<FavoriteResponse> findAllFavoriteResponses(Member member) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        Set<Favorite> favorites = member.getFavorites();
        for (Favorite favorite : favorites) {
            Station sourceStation = stationRepository.findById(favorite.getSourceStationId()).orElseThrow(NoSuchStationException::new);
            Station targetStation = stationRepository.findById(favorite.getTargetStationId()).orElseThrow(NoSuchStationException::new);
            String sourceStationName = sourceStation.getName();
            String targetStationName = targetStation.getName();
            favoriteResponses.add(FavoriteResponse.of(favorite, sourceStationName, targetStationName));
        }
        return favoriteResponses;
    }

    public Member addFavorite(Long memberId, FavoriteCreateRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
        Long sourceStationId = stationRepository.findIdByName(request.getSourceStationName());
        Long targetStationId = stationRepository.findIdByName(request.getTargetStationName());
        Favorite favorite = new Favorite(sourceStationId, targetStationId);
        member.addFavorite(favorite);
        return memberRepository.save(member);
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchMemberException::new);
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(NoSuchFavoriteException::new);
        member.removeFavorite(favorite);
        memberRepository.save(member);
    }
}
