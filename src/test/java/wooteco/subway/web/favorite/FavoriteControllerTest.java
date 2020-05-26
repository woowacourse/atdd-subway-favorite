package wooteco.subway.web.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.path.FavoritePath;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoritePathResponse;
import wooteco.subway.service.favorite.dto.FavoritePathsResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.web.FavoriteController;
import wooteco.subway.web.dto.ExceptionResponse;
import wooteco.subway.web.dto.FavoritePathRequest;
import wooteco.subway.web.member.AuthorizationExtractor;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = FavoriteController.class)
@AutoConfigureMockMvc
@Import({BearerAuthInterceptor.class, AuthorizationExtractor.class, JwtTokenProvider.class})
class FavoriteControllerTest {
	private static final String INVALID_TOKEN = "";
	private static final String NOT_DOCUMENTATION = "";

	@MockBean
	private FavoriteService favoriteService;

	@MockBean
	private MemberService memberService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${security.jwt.token.secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length}")
	private long validityInMilliseconds;

	@BeforeEach
	public void setup(WebApplicationContext webApplicationContext,
	                  RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
				.apply(documentationConfiguration(restDocumentation))
				.build();
	}

	@DisplayName("토큰이 유효하지 않는 경우 즐겨찾기 등록에 실패하는지 확인")
	@Test
	void authFailedWhenRegisterFavoritePath() throws Exception {
		FavoritePathRequest request = new FavoritePathRequest(STATION_NAME_KANGNAM, STATION_NAME_HANTI);
		String stringContent = objectMapper.writeValueAsString(request);

		MvcResult result = register(INVALID_TOKEN, stringContent, status().isUnauthorized(), "createError");

		String stringBody = result.getResponse().getContentAsString();
		ExceptionResponse response = objectMapper.readValue(stringBody, ExceptionResponse.class);
		assertThat(response.getErrorMessage()).isEqualTo("유효하지 않은 토큰입니다!");
	}

	@DisplayName("토큰이 유효하지 않는 경우 즐겨찾기 조회에 실패하는지 확인")
	@Test
	void authFailedWhenRetrieveFavoritePath() throws Exception {
		MvcResult result = retrieve(INVALID_TOKEN, status().isUnauthorized(), NOT_DOCUMENTATION);
		String stringBody = result.getResponse().getContentAsString();
		ExceptionResponse response = objectMapper.readValue(stringBody, ExceptionResponse.class);
		assertThat(response.getErrorMessage()).isEqualTo("유효하지 않은 토큰입니다!");
	}

	@DisplayName("토큰이 유효하지 않는 경우 즐겨찾기 삭제에 실패하는지 확인")
	@Test
	void authFailedWhenDeleteFavoritePath() throws Exception {
		MvcResult result = delete(INVALID_TOKEN, status().isUnauthorized(), "deleteError");

		String stringBody = result.getResponse().getContentAsString();
		ExceptionResponse response = objectMapper.readValue(stringBody, ExceptionResponse.class);
		assertThat(response.getErrorMessage()).isEqualTo("유효하지 않은 토큰입니다!");
	}

	@DisplayName("토큰이 유효하면 즐겨찾기 등록에 성공하는지 확인")
	@Test
	void registerFavoritePath() throws Exception {
		FavoritePathRequest request = new FavoritePathRequest(STATION_NAME_KANGNAM, STATION_NAME_HANTI);
		String stringContent = objectMapper.writeValueAsString(request);
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		BDDMockito.when(favoriteService.registerPath(member, STATION_NAME_KANGNAM, STATION_NAME_HANTI))
				.thenReturn(new FavoritePath(1L, 1L, 2L));

		String token =
				"bearer " + new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL);
		MvcResult result = register(token, stringContent, status().isCreated(), "create");

		assertThat(result.getResponse().getHeader("Location")).isNotNull();
	}

	private MvcResult register(String token, String content, ResultMatcher status, String identifier) throws Exception {
		return mockMvc.perform(
				post("/favorite/me")
						.header("Authorization", token)
						.content(content)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andDo(print())
				.andExpect(status)
				.andDo(FavoriteDocumentation.createFavorite(identifier))
				.andReturn();
	}

	@DisplayName("토큰이 유효하면 즐겨찾기 조회에 성공하는지 확인")
	@Test
	void retrieveFavoritePath() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station kangnam = new Station(1L, STATION_NAME_KANGNAM);
		Station hanti = new Station(2L, STATION_NAME_HANTI);
		Station dogok = new Station(3L, STATION_NAME_DOGOK);
		Station yangjae = new Station(4L, STATION_NAME_YANGJAE);

		BDDMockito.when(favoriteService.retrievePath(member))
				.thenReturn(Arrays.asList(new FavoritePathResponse(1L, StationResponse.of(kangnam),
				                                                   StationResponse.of(hanti)),
				                          new FavoritePathResponse(2L, StationResponse.of(dogok),
				                                                   StationResponse.of(yangjae))));
		BDDMockito.when(memberService.findMemberByEmail(TEST_USER_EMAIL)).thenReturn(member);

		String token =
				"bearer " + new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL);
		MvcResult result = retrieve(token, status().isOk(), "retrieve");

		String body = result.getResponse().getContentAsString();
		FavoritePathsResponse response = objectMapper.readValue(body, FavoritePathsResponse.class);

		assertThat(response.getFavoritePaths()).hasSize(2);
		assertThat(response.getFavoritePaths().get(0).getSource().getId()).isEqualTo(1L);
	}

	private MvcResult retrieve(String token, ResultMatcher status, String identifier) throws Exception {
		ResultActions actions = mockMvc.perform(
				MockMvcRequestBuilders.get("/favorite/me")
						.header("Authorization", token))
				.andDo(print());
		if (!NOT_DOCUMENTATION.equals(identifier)) {
			actions = actions.andDo(FavoriteDocumentation.retrieveFavorite(identifier));
		}
		return actions
				.andExpect(status)
				.andReturn();
	}

	@DisplayName("토큰이 유효하면 즐겨찾기 삭제에 성공하는지 확인")
	@Test
	void deleteFavoritePath() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Station dogok = new Station(3L, STATION_NAME_DOGOK);
		Station yangjae = new Station(4L, STATION_NAME_YANGJAE);

		BDDMockito.when(favoriteService.retrievePath(member))
				.thenReturn(Arrays.asList(new FavoritePathResponse(2L, StationResponse.of(dogok),
				                                                   StationResponse.of(yangjae))));
		BDDMockito.when(memberService.findMemberByEmail(TEST_USER_EMAIL)).thenReturn(member);

		String token =
				"bearer " + new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL);
		delete(token, status().isNoContent(), "delete");

		MvcResult result = retrieve(token, status().isOk(), "retrieve");
		String body = result.getResponse().getContentAsString();
		FavoritePathsResponse response = objectMapper.readValue(body, FavoritePathsResponse.class);

		assertThat(response.getFavoritePaths()).hasSize(1);
		assertThat(response.getFavoritePaths().get(0).getSource().getId()).isEqualTo(3L);
	}

	private MvcResult delete(String token, ResultMatcher statusCode, String identifier) throws Exception {
		return mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/favorite/me/{id}", 1)
						.header("Authorization", token))
				.andDo(FavoriteDocumentation.deleteFavorite(identifier))
				.andDo(print())
				.andExpect(statusCode)
				.andReturn();
	}
}