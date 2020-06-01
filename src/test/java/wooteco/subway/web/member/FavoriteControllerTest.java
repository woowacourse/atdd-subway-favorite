package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
	private static final String TARGET = "선릉역";
	private static final String SOURCE = "강남역";
	private static final String TOKEN = "This.is.token";

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
		FavoriteRequest request = new FavoriteRequest(SOURCE, TARGET);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).createFavorite(any(), any());

		mockMvc.perform(post("/me/favorites")
			.header("Authorization", "Bearer " + TOKEN)
			.contentType(MediaType.APPLICATION_JSON)
			.content(GSON.toJson(request)))
		       .andExpect(status().isNoContent())
		       .andDo(FavoriteDocumentation.createFavorite());
	}

	@Test
	void getFavorites() throws Exception {
		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		given(favoriteService.findFavorites(any())).willReturn(Arrays.asList(new FavoriteResponse(SOURCE, TARGET)));

		mockMvc.perform(get("/me/favorites")
			.header("Authorization", "Bearer " + TOKEN)
			.accept(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$[0].source", Matchers.is(SOURCE)))
		       .andExpect(jsonPath("$[0].target", Matchers.is(TARGET)))
		       .andDo(FavoriteDocumentation.getAllFavorites());
	}

	@Test
	void deleteFavorite() throws Exception {
		FavoriteRequest request = new FavoriteRequest(SOURCE, TARGET);

		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		doNothing().when(favoriteService).deleteFavorite(any(), any());

		mockMvc.perform(delete("/me/favorites")
			.header("Authorization", "Bearer " + TOKEN)
			.content(GSON.toJson(request))
			.contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNoContent())
		       .andDo(FavoriteDocumentation.deleteFavorite());
	}
}
