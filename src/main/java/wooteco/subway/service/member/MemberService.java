package wooteco.subway.service.member;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;

import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.NotFoundUserException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository,
        StationRepository stationRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member createMember(MemberRequest memberRequest) {
        return memberRepository.save(memberRequest.toMember());
    }

    public void updateMember(String email, Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id)
            .orElseThrow(RuntimeException::new);
        if (!email.equals(member.getEmail())) {
            throw new IllegalAccessError("잘못된 접근입니다.");
        }
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail())
            .orElseThrow(NotFoundUserException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new IllegalArgumentException("잘못된 패스워드 입니다.");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(NotFoundUserException::new);
    }

    public void addFavorite(Member member, FavoriteRequest favoriteRequest) {
        Station source = stationRepository.findByName(favoriteRequest.getSource())
            .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다."));
        Station target = stationRepository.findByName(favoriteRequest.getTarget())
            .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다."));
        Favorite favorite = new Favorite(source.getId(), target.getId());

        member.addFavorite(favorite);

        memberRepository.save(member);
    }

    public Set<FavoriteResponse> findFavorites(Member member) {
        Set<Favorite> favorites = member.getFavorites();
        Set<FavoriteResponse> favoriteResponses = new LinkedHashSet<>();
        for (Favorite favorite : favorites) {
            FavoriteResponse favoriteResponse = new FavoriteResponse(
                favorite.getId(),
                stationRepository.findById(favorite.getSourceStationId())
                    .orElseThrow(IllegalArgumentException::new).getName(),
                stationRepository.findById(favorite.getTargetStationId())
                    .orElseThrow(IllegalArgumentException::new).getName()
            );
            favoriteResponses.add(favoriteResponse);
        }
        return favoriteResponses;
    }

    public void deleteFavorites(Long favoriteId, Member member) {
        member.deleteFavoriteBy(favoriteId);
        memberRepository.save(member);
    }
}
