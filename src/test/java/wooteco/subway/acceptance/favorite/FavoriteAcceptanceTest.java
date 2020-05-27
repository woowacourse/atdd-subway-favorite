package wooteco.subway.acceptance.favorite;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.AcceptanceTest;
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
	// Then 즐겨찾기 목록을 n개 받아온다.
	//
	// When 즐겨찾기 제거 요청을 한다.
	// Then 즐겨찾기가 삭제 되었다.
	//
	// When 즐겨찾기 목록 조회 요청을 한다.
	// Then 즐겨찾기 목록을 n-1개 받아온다.

	@DisplayName("즐겨찾기 기능")
	@Test
	void manageFavorite() {
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		TokenResponse tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

        createStation(TEST_SOURCE);
        createStation(TEST_TARGET);

		String location = createFavorite(tokenResponse);

		assertThat(location).isEqualTo("/favorites/me");

		List<FavoriteResponse> favoriteResponses = getFavorites(tokenResponse);

		assertThat(favoriteResponses.size()).isEqualTo(1);
		assertThat(favoriteResponses.get(0).getSource()).isEqualTo(TEST_SOURCE);
		assertThat(favoriteResponses.get(0).getTarget()).isEqualTo(TEST_TARGET);

		deleteFavorite(tokenResponse);

		List<FavoriteResponse> favoriteResponses2 = getFavorites(tokenResponse);

		assertThat(favoriteResponses2).isEmpty();
	}
}
