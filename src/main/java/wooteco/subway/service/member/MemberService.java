package wooteco.subway.service.member;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
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
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public String createToken(LoginRequest param) {
        Member member = memberRepository.findByEmail(param.getEmail()).orElseThrow(RuntimeException::new);
        if (!member.checkPassword(param.getPassword())) {
            throw new RuntimeException("잘못된 패스워드");
        }

        return jwtTokenProvider.createToken(member.getId() + ":" + param.getEmail());
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    public boolean loginWithForm(String email, String password) {
        Member member = findMemberByEmail(email);
        return member.checkPassword(password);
    }

    public MemberFavoriteResponse findFavorites(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        Set<Favorite> favorites = member.getFavorites();
        Set<Long> stationIds = new HashSet<>();
        favorites.forEach(favorite -> {
            stationIds.add(favorite.getStartStationId());
            stationIds.add(favorite.getEndStationId());
        });

        List<Station> stations = stationRepository.findAllById(stationIds);

        Map<Long, Station> stationMap = new HashMap<>();

        stations.forEach(station -> stationMap.put(station.getId(), station));

        Set<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(it ->
                        new FavoriteResponse(it.getId(),
                                stationMap.get(it.getStartStationId()),
                                stationMap.get(it.getEndStationId())))
                .collect(Collectors.toSet());

        return new MemberFavoriteResponse(member.getId(), favoriteResponses);
    }

    public void addFavorite(Long id, FavoriteCreateRequest favoriteCreateRequest) {
        Member member = memberRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        member.addFavorite(new Favorite(favoriteCreateRequest.getStartStationId(),
                favoriteCreateRequest.getEndStationId()));
        memberRepository.save(member);
    }


    public void deleteFavoriteById(Long memberId, Long favoriteId) {
        Member member = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        member.removeFavorite(favoriteId);
        memberRepository.save(member);
    }
}
