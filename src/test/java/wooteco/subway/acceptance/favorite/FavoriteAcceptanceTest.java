package wooteco.subway.acceptance.favorite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.TokenResponse;

public class FavoriteAcceptanceTest extends AcceptanceTest {
    // Feature: 즐겨찾기
    //
    // Scenario: 즐겨찾기 기능을 사용한다.
    // Given 회원가입과 로그인이 되어있는 상태이다.
    //
    // When 즐겨찾기 추가 요청을 한다.
    // Then 즐겨찾기가 추가 되었다.
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
        prepareData();
        TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(강남역, 역삼역);
        createFavorite(tokenResponse, favoriteCreateRequest);

        List<FavoriteResponse> favoriteResponses = getFavorites(tokenResponse);
        assertThat(favoriteResponses).hasSize(1);
        assertThat(favoriteResponses.get(0).getSource()).isEqualTo(강남역);
        assertThat(favoriteResponses.get(0).getTarget()).isEqualTo(역삼역);

        FavoriteCreateRequest favoriteCreateRequest2
            = new FavoriteCreateRequest(강남역, 선릉역);
        createFavorite(tokenResponse, favoriteCreateRequest2);

        List<FavoriteResponse> favoriteResponses2 = getFavorites(tokenResponse);
        assertThat(favoriteResponses2.size()).isEqualTo(2);
        assertThat(favoriteResponses2.get(1).getSource()).isEqualTo(강남역);
        assertThat(favoriteResponses2.get(1).getTarget()).isEqualTo(선릉역);

        FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest(강남역, 역삼역);
        deleteFavorite(tokenResponse, favoriteDeleteRequest);

        List<FavoriteResponse> favoriteResponses3 = getFavorites(tokenResponse);
        assertThat(favoriteResponses3).hasSize(1);
        assertThat(favoriteResponses3.get(0).getSource()).isEqualTo(강남역);
        assertThat(favoriteResponses3.get(0).getTarget()).isEqualTo(선릉역);
    }

    private void prepareData() {
        createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        createStation(강남역);
        createStation(역삼역);
        createStation(선릉역);
    }
}
