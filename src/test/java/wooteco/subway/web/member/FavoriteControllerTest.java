package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.acceptance.AcceptanceTest.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.config.ETagHeaderFilter;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.interceptor.BearerAuthInterceptor;

@Import(ETagHeaderFilter.class)
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {

	@Autowired
	protected MockMvc mockMvc;
	Member member;
	@MockBean
	private FavoriteService favoriteService;
	@MockBean
	private MemberService memberService;
	@MockBean
	private BearerAuthInterceptor bearerAuthInterceptor;
	@MockBean
	private LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
		given(loginMemberMethodArgumentResolver.resolveArgument(any(), any(), any(),
			any())).willReturn(member);
		given(loginMemberMethodArgumentResolver.supportsParameter(any())).willReturn(true);
		given(memberService.createToken(any())).willReturn("brownToken");

		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@DisplayName("즐겨찾기에 경로를 추가한다")
	@Test
	void addFavorite() throws Exception {
		given(favoriteService.add(any(), any())).willReturn(1L);

		String inputJson = "{\"sourceStationId\":" + 1 + "," + "\"targetStationId\":" + 2 + "}";

		this.mockMvc.perform(post("/favorites")
			.header("Authorization", "bearer brownToken")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated())
			.andDo(FavoriteDocumentation.addFavorite());
	}

	@DisplayName("즐겨찾기에 있는 경로를 삭제한다")
	@Test
	void deleteFavorites() throws Exception {
		this.mockMvc.perform(delete("/favorites/{id}", "1")
			.header("Authorization", "bearer brownToken")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(FavoriteDocumentation.deleteFavorite());
	}

	@DisplayName("즐겨찾기 목록을 조회한다")
	@Test
	void getFavorites() throws Exception {
		given(favoriteService.findFavorites(any())).willReturn(
			Arrays.asList(new FavoriteResponse(STATION_NAME_YANGJAE, STATION_NAME_YEOKSAM),
				new FavoriteResponse(STATION_NAME_KANGNAM, STATION_NAME_HANTI)));

		this.mockMvc.perform(get("/favorites")
			.header("Authorization", "bearer brownToken")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(FavoriteDocumentation.getFavorites());
	}
}
