package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.doc.MemberLoginDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.vo.FavoriteInfo;

/**
 *    class description
 *
 *    @author HyungJu An
 */
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = {FavoriteController.class, MemberController.class, LoginMemberController.class})
@Import({JwtTokenProvider.class, AuthorizationExtractor.class})
public class FavoriteControllerTest {
	@MockBean
	protected MemberService memberService;

	@MockBean
	protected FavoriteService favoriteService;

	@Autowired
	protected JwtTokenProvider jwtTokenProvider;

	@Autowired
	protected AuthorizationExtractor authorizationExtractor;

	protected MockMvc mockMvc;

	private String email;
	private String password;
	private String name;
	private String token;
	private Member member;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.addFilter((req, res, chain) -> {
				res.setCharacterEncoding("UTF-8");
				chain.doFilter(req, res);
			}, "/*")
			.apply(documentationConfiguration(restDocumentation))
			.build();

		email = "a@email.com";
		password = "1234";
		name = "asdf";
		token = jwtTokenProvider.createToken(email);
		member = Member.of(email, name, password).withId(1L);
	}

	@Test
	void addFavorite() throws Exception {
		final long sourceId = 1L;
		final long targetId = 2L;
		given(memberService.findMemberByEmail(any())).willReturn(member);

		String inputJson = "{\"sourceId\":\"" + sourceId + "\"," +
			"\"targetId\":\"" + targetId + "\"}";

		this.mockMvc.perform(post("/favorites")
			.header("authorization", "Bearer " + token)
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isCreated())
			.andDo(print())
			.andDo(MemberLoginDocumentation.addFavorite());
	}

	@DisplayName("출발역이나 도착역 id가 null이면 예외처리한다.")
	@Test
	void addFavorite2() throws Exception {
		final Long sourceId = null;
		final Long targetId = 2L;
		given(memberService.findMemberByEmail(any())).willReturn(member);

		String inputJson = "{\"sourceId\":\"" + sourceId + "\"," +
			"\"targetId\":\"" + targetId + "\"}";

		this.mockMvc.perform(post("/favorites")
			.header("authorization", "Bearer " + token)
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	void getFavorites() throws Exception {
		final List<FavoriteInfo> favoriteInfos = Arrays.asList(
			new FavoriteInfo(Station.of("강남역").withId(1L), Station.of("잠실역").withId(2L)),
			new FavoriteInfo(Station.of("청계산입구역").withId(3L), Station.of("대모산입구역").withId(4L)));
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(favoriteService.getFavoriteInfos(any())).willReturn(favoriteInfos);

		this.mockMvc.perform(get("/favorites")
			.header("authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberLoginDocumentation.getFavorites());
	}

	@Test
	void deleteFavorite() throws Exception {
		given(memberService.findMemberByEmail(any())).willReturn(member);
		final Long sourceId = 1L;
		final Long targetId = 2L;
		member.addFavorite(Favorite.of(sourceId, targetId));
		member.addFavorite(Favorite.of(3L, 4L));

		this.mockMvc.perform(delete("/favorites?sourceId=" + sourceId + "&targetId=" + targetId)
			.header("authorization", "Bearer " + token))
			.andExpect(status().isNoContent())
			.andDo(print())
			.andDo(MemberLoginDocumentation.deleteFavorite());
	}
}
