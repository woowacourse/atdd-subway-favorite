package wooteco.subway.acceptance.member;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import wooteco.subway.AcceptanceTest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.TokenResponse;

/**
 *    class description
 *
 *    @author HyungJu An
 */
public class FavoriteAcceptanceTest extends AcceptanceTest {
	/*
	 * Feature : 로그인 멤버의 즐겨찾기 관리
	 *
	 * Scenario : 로그인 멤버의 즐겨찾기를 관리한다.
	 * Given: 로그인이 되어있다.
	 * And: 지하철 역이 추가되어있다.
	 *
	 * When : 즐겨찾기 추가를 요청한다.
	 * Then : 즐겨찾기가 추가된다.
	 *
	 * When : 즐겨찾기 정보 조회 요청한다.
	 * Then : 즐겨찾기 정보를 응답받는다.
	 *
	 * When : 즐겨찾기 삭제 요청을 한다.
	 * Then : 즐겨찾기가 삭제된다.
	 */
	@Test
	void manageFavorite() {
		createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		createStation("강남역");
		createStation("잠실역");
		final TokenResponse tokenResponse = login(new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD));

		addFavorite(tokenResponse.getAccessToken(), 1L, 2L);

		final List<FavoriteResponse> favorites = retrieveFavorites(tokenResponse.getAccessToken());
		assertThat(favorites).hasSize(1);
	}
}
