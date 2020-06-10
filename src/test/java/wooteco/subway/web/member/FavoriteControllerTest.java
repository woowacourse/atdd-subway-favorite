package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.BearerAuthInterceptorTest.*;
import static wooteco.subway.acceptance.AcceptanceTest.*;
import static wooteco.subway.web.AuthorizationExtractor.*;
import static wooteco.subway.web.BearerAuthInterceptor.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.google.gson.Gson;
import wooteco.subway.docs.FavoriteDocumentation;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.web.BearerAuthInterceptor;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

	private static final Gson GSON = new Gson();

	@MockBean
	private FavoriteService favoriteService;

	@MockBean
	private BearerAuthInterceptor bearerAuthInterceptor;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
		                              .addFilter(new ShallowEtagHeaderFilter())
		                              .apply(documentationConfiguration(restDocumentation))
		                              .addFilters(new CharacterEncodingFilter("UTF-8", true))
		                              .alwaysDo(print())
		                              .build();
	}

	@Test
	void createFavorite() throws Exception {
		FavoriteRequest request = new FavoriteRequest(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).createFavorite(any(), any());

		mockMvc
			.perform(post("/me/favorites")
				.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
				.contentType(MediaType.APPLICATION_JSON)
				.content(GSON.toJson(request)))
			.andExpect(status().isNoContent())
			.andDo(FavoriteDocumentation.createFavorite());
	}

	@ParameterizedTest
	@NullAndEmptySource
	void createFavorite_BlankSourceInRequest(final String source) throws Exception {
		FavoriteRequest request = new FavoriteRequest(source, STATION_NAME_SEOLLEUNG);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).createFavorite(any(), any());

		mockMvc
			.perform(post("/me/favorites")
				.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
				.contentType(MediaType.APPLICATION_JSON)
				.content(GSON.toJson(request)))
			.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@NullAndEmptySource
	void createFavorite_BlankTargetInRequest(final String target) throws Exception {
		FavoriteRequest request = new FavoriteRequest(STATION_NAME_KANGNAM, target);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).createFavorite(any(), any());

		mockMvc
			.perform(post("/me/favorites")
				.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
				.contentType(MediaType.APPLICATION_JSON)
				.content(GSON.toJson(request)))
			.andExpect(status().isBadRequest());
	}

	@Test
	void getFavorites() throws Exception {
		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		given(favoriteService.findFavorites(any())).willReturn(Arrays.asList(
			new FavoriteResponse(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG)));

		mockMvc
			.perform(get("/me/favorites")
				.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].source", Matchers.is(STATION_NAME_KANGNAM)))
			.andExpect(
				jsonPath("$[0].target", Matchers.is(STATION_NAME_SEOLLEUNG)))
			.andDo(FavoriteDocumentation.getAllFavorites());
	}

	@Test
	void deleteFavorite() throws Exception {
		FavoriteRequest request = new FavoriteRequest(STATION_NAME_KANGNAM, STATION_NAME_SEOLLEUNG);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).deleteFavorite(any(), any());

		mockMvc
			.perform(delete("/me/favorites")
				.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
				.content(GSON.toJson(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andDo(FavoriteDocumentation.deleteFavorite());
	}

	@ParameterizedTest
	@NullAndEmptySource
	void deleteFavorite_BlankSourceInRequest(final String source) throws Exception {
		FavoriteRequest request = new FavoriteRequest(source, STATION_NAME_SEOLLEUNG);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).createFavorite(any(), any());

		mockMvc
			.perform(delete("/me/favorites")
				.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
				.contentType(MediaType.APPLICATION_JSON)
				.content(GSON.toJson(request)))
			.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@NullAndEmptySource
	void deleteFavorite_BlankTargetInRequest(final String target) throws Exception {
		FavoriteRequest request = new FavoriteRequest(STATION_NAME_KANGNAM, target);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).createFavorite(any(), any());

		mockMvc
			.perform(delete("/me/favorites")
				.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
				.contentType(MediaType.APPLICATION_JSON)
				.content(GSON.toJson(request)))
			.andExpect(status().isBadRequest());
	}

}
