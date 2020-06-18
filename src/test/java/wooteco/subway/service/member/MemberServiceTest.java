package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import wooteco.subway.domain.member.LoginEmail;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.member.favorite.Favorite;
import wooteco.subway.domain.member.favorite.FavoriteRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteDeleteRequest;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponses;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository, stationRepository,
            jwtTokenProvider, favoriteRepository);
    }

    @DisplayName("회원 가입 기능")
    @Test
    void createMember() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME,
            TEST_USER_PASSWORD);
        when(memberRepository.save(any())).thenReturn(
            new Member(1L, memberRequest.getEmail(), memberRequest.getName(),
                memberRequest.getPassword()));
        memberService.createMember(memberRequest);

        verify(memberRepository, times(1)).save(any());
    }

    @DisplayName("로그인 후 토큰 생성")
    @Test
    void createToken() {
        Member member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        memberService.createToken(loginRequest);

        verify(jwtTokenProvider).createToken(TEST_USER_EMAIL);
    }

    @DisplayName("회원 정보 수정")
    @Test
    void update() {
        //given
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("NEW_" + TEST_USER_NAME,
            "NEW_" + TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(TEST_USER_EMAIL))
            .thenReturn(
                Optional.of(new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD)));

        //when
        memberService.updateMember(updateMemberRequest, new LoginEmail(TEST_USER_EMAIL));

        //then
        verify(memberRepository).save(
            new Member(1L, TEST_USER_EMAIL, "NEW_" + TEST_USER_NAME, "NEW_" + TEST_USER_PASSWORD));
    }

    @DisplayName("회원 탈퇴")
    @Test
    void delete() {
        //given
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(TEST_USER_EMAIL))
            .thenReturn(Optional.of(member));

        //when
        memberService.deleteByEmail(new LoginEmail(TEST_USER_EMAIL));

        //then
        verify(memberRepository).delete(member);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        LoginEmail loginEmail = new LoginEmail(TEST_USER_EMAIL);
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(memberRepository.findByEmail(TEST_USER_EMAIL))
            .thenReturn(Optional.of(member));
        when(stationRepository.findAllById(Arrays.asList(1L, 2L)))
            .thenReturn(Arrays.asList(new Station(1L, "123"), new Station(2L, "456")));
        when(favoriteRepository.save(any())).thenReturn(
            new Favorite(1L, member, new Station(1L, "123"), new Station(2L, "456")));

        //when
        memberService.addFavorite(favoriteRequest, loginEmail);

        //then
        assertThat(member.getFavorites().getValues().get(0).getId()).isEqualTo(1L);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        //given
        FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest(1L, 2L);
        LoginEmail loginEmail = new LoginEmail(TEST_USER_EMAIL);
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Favorite favorite = new Favorite(1L, member, new Station(1L, "잠실역"),
            new Station(2L, "역삼역"));
        member.addFavorite(favorite);

        when(memberRepository.findByEmail(TEST_USER_EMAIL)).thenReturn(Optional.of(member));
        when(favoriteRepository.findByMemberIdAndSourceStationIdAndTargetStationId(any(), any(),
            any())).thenReturn(Optional.of(favorite));

        //when
        memberService.deleteFavorite(favoriteDeleteRequest, loginEmail);

        //then
        verify(favoriteRepository).deleteById(1L);
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void getFavorites() {
        //given
        LoginEmail loginEmail = new LoginEmail(TEST_USER_EMAIL);

        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        member.addFavorite(
            new Favorite(1L, member, new Station(1L, "잠실역"), new Station(2L, "역삼역")));
        member.addFavorite(
            new Favorite(2L, member, new Station(2L, "역삼역"), new Station(3L, "강변역")));

        when(memberRepository.findByEmail(TEST_USER_EMAIL))
            .thenReturn(Optional.of(member));

        //when
        FavoriteResponses allFavorites = memberService.getAllFavorites(loginEmail);

        //then
        assertThat(allFavorites.getFavoriteResponses()).hasSize(2);
    }
}
