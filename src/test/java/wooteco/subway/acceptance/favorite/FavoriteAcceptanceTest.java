package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    // Feature: 즐겨찾기
    //
    // Scenario: 즐겨찾기 기능을 사용한다.
    // Given 회원가입과 로그인이 되어있는 상태이다.
    //
    // When 즐겨찾기 추가 요청을 한다.
    // Then 즐겨찾기가 추가 되었다.
    //
    // When 즐겨찾기 목록 조회 요청을 한다.
    // Then 즐겨찾기 목록을 n+1개 받아온다.
    //
    // When 즐겨찾기 추가 요청을 한다.
    // Then 즐겨찾기가 추가 되었다.
    //
    // When 즐겨찾기 목록 조회 요청을 한다.
    // Then 즐겨찾기 목록을 n+2개 받아온다.
    //
    // When 즐겨찾기 제거 요청을 한다.
    // Then 즐겨찾기가 삭제 되었다.
    //
    // When 즐겨찾기 목록 조회 요청을 한다.
    // Then 즐겨찾기 목록을 n+1개 받아온다.

    @DisplayName("즐겨찾기 기능")
    @Test
    void manageFavorite() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);

        //즐겨찾기 추가1
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(STATION_NAME_KANGNAM, STATION_NAME_YEOKSAM);
        String location = createFavorite(tokenResponse, favoriteCreateRequest);
        assertThat(location).isEqualTo("/favorites/me");
        
        //조회
        List<FavoriteResponse> favoriteResponses = getFavorites(tokenResponse);
        assertThat(favoriteResponses).hasSize(1);
        assertThat(favoriteResponses.get(0).getSource()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(favoriteResponses.get(0).getTarget()).isEqualTo(STATION_NAME_YEOKSAM);

        //즐겨찾기 추가2
        FavoriteCreateRequest favoriteCreateRequest2 = new FavoriteCreateRequest(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG);
        String location2 = createFavorite(tokenResponse, favoriteCreateRequest2);
        assertThat(location2).isEqualTo("/favorites/me");

        //조회
        List<FavoriteResponse> favoriteResponses2 = getFavorites(tokenResponse);
        assertThat(favoriteResponses2.size()).isEqualTo(2);
        assertThat(favoriteResponses2.get(1).getSource()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(favoriteResponses2.get(1).getTarget()).isEqualTo(STATION_NAME_SEOLLEUNG);

        //즐겨찾기 제거
        FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest(STATION_NAME_KANGNAM, STATION_NAME_YEOKSAM);
        deleteFavorite(tokenResponse, favoriteDeleteRequest);
        List<FavoriteResponse> favoriteResponses3 = getFavorites(tokenResponse);

        //조회
        assertThat(favoriteResponses3).hasSize(1);
        assertThat(favoriteResponses3.get(0).getSource()).isEqualTo(STATION_NAME_KANGNAM);
        assertThat(favoriteResponses3.get(0).getTarget()).isEqualTo(STATION_NAME_SEOLLEUNG);
    }
}
