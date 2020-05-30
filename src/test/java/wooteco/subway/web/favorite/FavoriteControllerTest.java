package wooteco.subway.web.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.AcceptanceTest.*;
import static wooteco.subway.web.member.interceptor.BearerAuthInterceptor.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteListResponse;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.AuthorizationExtractor;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Sql("/truncate.sql")
public class FavoriteControllerTest {

	@MockBean
	private FavoriteService favoriteService;
	@MockBean
	private MemberService memberService;
	@MockBean
	private AuthorizationExtractor authExtractor;
	@MockBean
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper objectMapper;
	private Member member;
	private String credential = " secret";
	private String mockToken = BEARER + credential;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(print())
			.build();
		this.member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		objectMapper = new ObjectMapper();
	}

	@Test
	void addFavorite() throws Exception {
		setMockToken();
		FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(1L, 2L);
		String request = objectMapper.writeValueAsString(favoriteCreateRequest);
		FavoriteResponse response = new FavoriteResponse(1L, favoriteCreateRequest.getDepartureId(),
			favoriteCreateRequest.getDepartureId());
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(favoriteService.addFavorite(any(), any())).willReturn(response);

		this.mockMvc.perform(post("/members/favorites")
			.header(HttpHeaders.AUTHORIZATION, mockToken)
			.content(request)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andDo(MemberDocumentation.addFavorite());
	}

	public void setMockToken() {
		given(authExtractor.extract(any(), eq(BEARER))).willReturn(credential);
		given(jwtTokenProvider.validateToken(credential)).willReturn(true);
		given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
	}

	@Test
	void findFavorites() throws Exception {
		//Given
		setMockToken();
		FavoriteResponse response = new FavoriteResponse(1L, 1L, 2L);
		FavoriteResponse response2 = new FavoriteResponse(2L, 3L, 4L);
		FavoriteListResponse favoriteListResponse = new FavoriteListResponse(
			Arrays.asList(response, response2));

		given(favoriteService.findFavorites(any())).willReturn(favoriteListResponse);

		//When
		MvcResult mvcResult = this.mockMvc.perform(get("/members/favorites")
			.header(HttpHeaders.AUTHORIZATION, mockToken)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(MemberDocumentation.findFavorites())
			.andReturn();

		//Then
		ObjectMapper mapper = new ObjectMapper();
		assertThat(mapper.readValue(
			mvcResult.getResponse().getContentAsString(), FavoriteListResponse.class)).isInstanceOf(
			FavoriteListResponse.class);
	}

	@Test
	void deleteFavorite() throws Exception {
		setMockToken();
		Favorite favorite = new Favorite(1L, 1L, 2L);

		this.mockMvc.perform(delete("/members/favorites/{id}", favorite.getId())
			.header(HttpHeaders.AUTHORIZATION, mockToken))
			.andExpect(status().isNoContent())
			.andDo(MemberDocumentation.deleteFavorite());
	}
}
