package wooteco.subway.web.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.TEST_USER_EMAIL;
import static wooteco.subway.AcceptanceTest.TEST_USER_NAME;
import static wooteco.subway.AcceptanceTest.TEST_USER_PASSWORD;
import static wooteco.subway.web.FavoriteController.FAVORITE_URI;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteListResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.ControllerTest;

class FavoriteControllerTest extends ControllerTest {
	@MockBean
	private FavoriteService favoriteService;

	@Test
	void addFavorite() throws Exception {
		FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(1L, 2L);
		String request = objectMapper.writeValueAsString(favoriteCreateRequest);
		FavoriteResponse response = new FavoriteResponse(1L, favoriteCreateRequest.getDepartureId(),
			favoriteCreateRequest.getDepartureId());
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(favoriteService.addFavorite(any(), any())).willReturn(response);

		postWithAuth(FAVORITE_URI, request, status().isCreated(), MemberDocumentation.addFavorite());
	}

	@Test
	void findFavorites() throws Exception {
		//Given
		FavoriteResponse response = new FavoriteResponse(1L, 1L, 2L);
		FavoriteResponse response2 = new FavoriteResponse(2L, 3L, 4L);
		FavoriteListResponse favoriteListResponse = new FavoriteListResponse(
			Arrays.asList(response, response2));

		given(favoriteService.findFavorites(any())).willReturn(favoriteListResponse);

		//When
		MvcResult mvcResult = getWithAuth(FAVORITE_URI, MemberDocumentation.findFavorites());

		//Then
		assertThat(objectMapper.readValue(
			mvcResult.getResponse().getContentAsString(), FavoriteListResponse.class)).isInstanceOf(
			FavoriteListResponse.class);
	}

	@Test
	void deleteFavorite() throws Exception {
		Favorite favorite = new Favorite(1L, 1L, 2L);
		deleteWithAuth(FAVORITE_URI + "/" + favorite.getId(), MemberDocumentation.deleteFavorite());
	}
}
