package wooteco.subway.service.member;

import java.util.Arrays;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.member.LoginEmail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.domain.member.favorite.Favorites;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.exception.DuplicatedEmailException;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponses;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.web.dto.ErrorCode;
import wooteco.subway.web.member.exception.MemberException;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(final MemberRepository memberRepository,
        final StationRepository stationRepository, final JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public Long createMember(MemberRequest memberRequest) {
        Member member = new Member(memberRequest.getEmail(), memberRequest.getName(),
            memberRequest.getPassword());
        validateName(member);
        try {
            return memberRepository.save(member).getId();
        } catch (DuplicateKeyException e) {
            throw new DuplicatedEmailException(member.getEmail());
        }
    }

    private void validateName(final Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new DuplicatedEmailException(member.getEmail());
        }
    }

    @Transactional
    public void updateMember(UpdateMemberRequest param, LoginEmail loginEmail) {
        Member member = getMember(loginEmail);
        member.update(param.getName(), param.getPassword());
        memberRepository.save(member);
    }

    public String createToken(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException(String.format("%s : 가입하지 않은 이메일입니다.", email),
                ErrorCode.UNSIGNED_EMAIL));

        if (!member.checkPassword(loginRequest.getPassword())) {
            throw new MemberException(ErrorCode.WRONG_PASSWORD);
        }

        return jwtTokenProvider.createToken(loginRequest.getEmail());
    }

    private Member getMember(final LoginEmail loginEmail) {
        String email = loginEmail.getEmail();
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new MemberException(String.format("%s : 가입하지 않은 이메일입니다.", email),
                ErrorCode.UNSIGNED_EMAIL));
    }

    public Member findMemberByEmail(final LoginEmail loginEmail) {
        return getMember(loginEmail);
    }

    @Transactional
    public void deleteByEmail(final LoginEmail loginEmail) {
        Member member = getMember(loginEmail);
        memberRepository.delete(member);
    }

    @Transactional
    public void addFavorite(final FavoriteRequest favoriteRequest, LoginEmail loginEmail) {
        Member member = getMember(loginEmail);
        Stations stations = new Stations(
            stationRepository.findAllById(Arrays.asList(favoriteRequest.getSourceStationId(),
                favoriteRequest.getTargetStationId())));
        Station source = stations.extractStationById(favoriteRequest.getSourceStationId());
        Station target = stations.extractStationById(favoriteRequest.getTargetStationId());
        Favorite favorite = new Favorite(source.getId(), target.getId());
        member.addFavorite(favorite);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteFavorite(final FavoriteDeleteRequest deleteRequest,
        final LoginEmail loginEmail) {
        Favorite favorite = new Favorite(deleteRequest.getSourceStationId(),
            deleteRequest.getTargetStationId());
        Member member = getMember(loginEmail);
        member.removeFavorite(favorite);
        memberRepository.save(member);
    }

    public FavoriteResponses getAllFavorites(final LoginEmail loginEmail) {
        Member member = getMember(loginEmail);
        Favorites favorites = member.getFavorites();
        Stations stations = new Stations(stationRepository.findAllById(favorites.getStationIds()));

        return FavoriteResponses.of(favorites, stations.toMap());
    }
}
