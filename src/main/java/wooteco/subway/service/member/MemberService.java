package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.exception.LoginFailException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;
    private StationRepository stationRepository;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.stationRepository = stationRepository;
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public void updateMember(Long id, UpdateMemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 id를 찾을 수 없습니다."));
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(() -> new LoginFailException("해당 email를 찾을 수 없습니다."));
        if (!member.checkPassword(param.getPassword())) {
            throw new LoginFailException("잘못된 패스워드");
        }

        return jwtTokenProvider.createToken(param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("해당 email를 찾을 수 없습니다."));
    }

    public boolean loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        return member.checkPassword(password);
    }

    public Set<FavoriteResponse> findFavorites(Member member) {
        Set<Favorite> favorites = member.getFavorites();
        Set<Long> stationIds = new HashSet<>();
        favorites.forEach(favorite -> {
            stationIds.add(favorite.getStartStationId());
            stationIds.add(favorite.getEndStationId());
        });

        List<Station> stations = stationRepository.findAllById(stationIds);

        Map<Long, Station> stationMap = new HashMap<>();

        stations.forEach(station -> stationMap.put(station.getId(), station));

        return favorites.stream()
                .map(favorite ->
                        FavoriteResponse.of(favorite.getId(),
                                stationMap.get(favorite.getStartStationId()),
                                stationMap.get(favorite.getEndStationId())))
                .collect(Collectors.toSet());
    }

    public void addFavorite(Member member, FavoriteCreateRequest favoriteCreateRequest) {
        member.addFavorite(Favorite.of(favoriteCreateRequest.getStartStationId(),
                favoriteCreateRequest.getEndStationId()));
        memberRepository.save(member);
    }


    public void deleteFavoriteById(Member member, Long id) {
        member.removeFavorite(id);
        memberRepository.save(member);
    }
}
