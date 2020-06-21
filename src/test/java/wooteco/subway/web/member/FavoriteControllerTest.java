package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.MemberLoginDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.dto.FavoriteResponse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * class description
 *
 * @author HyungJu An
 */
public class FavoriteControllerTest extends MemberDocumentationTest {
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
		member = Member.of(1L, email, name, password);
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
		final List<Favorite> favorites = Arrays.asList(
				Favorite.of(1L, Station.of(1L, "강남역"), Station.of(2L, "잠실역")),
				Favorite.of(2L, Station.of(3L, "청계산입구역"), Station.of(4L, "대모산입구역")));
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(favoriteService.getFavorites(any())).willReturn(FavoriteResponse.listOf(favorites));

		this.mockMvc.perform(get("/favorites")
				.header("authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(MemberLoginDocumentation.getFavorites());
	}

	@Test
	void deleteFavorite() throws Exception {
		given(memberService.findMemberByEmail(any())).willReturn(member);
		Station station1 = Station.of(1L, "의정부역");
		Station station2 = Station.of(1L, "회룡역");
		Station station3 = Station.of(1L, "망월사역");
		Station station4 = Station.of(1L, "도봉산역");
		Favorite favorite1 = Favorite.of(1L, station1, station2);
		Favorite favorite2 = Favorite.of(2L, station3, station4);
		member.addFavorite(favorite1);
		member.addFavorite(favorite2);
		Long id = favorite1.getId();

		this.mockMvc.perform(delete("/favorites/{id}", id)
				.header("authorization", "Bearer " + token))
				.andExpect(status().isNoContent())
				.andDo(print())
				.andDo(MemberLoginDocumentation.deleteFavorite());
	}
}
