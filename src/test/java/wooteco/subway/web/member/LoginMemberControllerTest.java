package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.doc.MemberLoginDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.dto.LoginRequest;

/**
 *    class description
 *
 *    @author HyungJu An, MinWoo Yim
 */

public class LoginMemberControllerTest extends MemberDocumentationTest {
	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.build();
	}

	@Test
	void login() throws Exception {
		final String email = "a@email.com";
		final String password = "1234";
		final LoginRequest loginRequest = new LoginRequest(email, password);
		final String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberLoginDocumentation.createToken());
	}

	@DisplayName("유저 email이 빈 값이면 예외 처리한다.")
	@Test
	void login2() throws Exception {
		final String email = "";
		final String password = "1234";
		final LoginRequest loginRequest = new LoginRequest(email, password);
		final String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("유저 email 형식이 잘못되면 예외 처리한다.")
	@Test
	void login3() throws Exception {
		final String email = "asdfasdf";
		final String password = "1234";
		final LoginRequest loginRequest = new LoginRequest(email, password);
		final String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@DisplayName("유저 password가 빈 값이면 예외 처리한다.")
	@Test
	void login4() throws Exception {
		final String email = "a@email.com";
		final String password = "";
		final LoginRequest loginRequest = new LoginRequest(email, password);
		final String token = jwtTokenProvider.createToken(loginRequest.getEmail());

		given(memberService.createToken(any())).willReturn(token);

		String inputJson = "{\"email\":\"" + email + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(post("/oauth/token")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print());
	}

	@Test
	void getMemberOfMineBasic() throws Exception {
		final String email = "a@email.com";
		final String password = "1234";
		final Member member = Member.of(email, "asdf", password).withId(1L);
		final String token = jwtTokenProvider.createToken(email);
		given(memberService.findMemberByEmail(any())).willReturn(member);

		this.mockMvc.perform(get("/me")
			.header("authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberLoginDocumentation.getMemberOfMineBasic());
	}

	@Test
	void getMemberOfMineBasicWithException() throws Exception {
		final String email = "a@email.com";
		final String password = "1234";

		this.mockMvc.perform(get("/me")
			.header("authorization", "Bearer abc"))
			.andExpect(status().isForbidden())
			.andDo(print());
	}

	@Test
	void updateMemberOfMineBasic() throws Exception {
		final String email = "a@email.com";
		final String password = "1234";
		final String name = "asdf";
		final String token = jwtTokenProvider.createToken(email);
		final Member member = Member.of(email, name, password).withId(1L);
		given(memberService.findMemberByEmail(any())).willReturn(member);

		String inputJson = "{\"name\":\"" + "brown" + "\"," +
			"\"password\":\"" + password + "\"}";

		this.mockMvc.perform(put("/me")
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.header("authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberLoginDocumentation.updateMemberOfMineBasic());
	}

	@Test
	void deleteMemberOfMineBasic() throws Exception {
		final String email = "a@email.com";
		final String password = "1234";
		final String name = "asdf";
		final String token = jwtTokenProvider.createToken(email);
		final Member member = Member.of(email, name, password).withId(1L);
		given(memberService.findMemberByEmail(any())).willReturn(member);

		this.mockMvc.perform(delete("/me")
			.header("authorization", "Bearer " + token))
			.andExpect(status().isNoContent())
			.andDo(print())
			.andDo(MemberLoginDocumentation.deleteMemberOfMineBasic());
	}
}