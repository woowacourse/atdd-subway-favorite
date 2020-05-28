package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.NoMemberExistException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.interceptor.AuthorizationExtractor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.service.member.MemberServiceTest.*;

@ExtendWith(RestDocumentationExtension.class)
//@WebMvcTest(controllers = {MemberController.class, LoginMemberController.class})
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
	public static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1OTAwNTA1NjMsImV4cCI6MTU5MDA1NDE2M30.bPh4VZcEj7aYlXDBP_o-1IqZw5AoKCIetrHvI7OcB_k";
	@MockBean
	protected MemberService memberService;
	@MockBean
	protected JwtTokenProvider jwtTokenProvider;
	@MockBean
	protected AuthorizationExtractor authorizationExtractor;
	@Autowired
	protected MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(webApplicationContext)
				.addFilter(new ShallowEtagHeaderFilter()).alwaysDo(print())
				.apply(documentationConfiguration(restDocumentation))
				.build();
	}

	@Test
	public void createMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		given(memberService.createMember(any())).willReturn(member);

		String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
				"\"name\":\"" + TEST_USER_NAME + "\"," +
				"\"password\":\"" + TEST_USER_PASSWORD + "\"}";

		this.mockMvc.perform(post("/members")
				.content(inputJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print())
				.andDo(MemberDocumentation.createMember());
	}

	@Test
	public void failToCreateMember() throws Exception {
		String inputJson = "{\"email\":\"" + "이상한이메일" + "\"," +
				"\"name\":\"" + TEST_USER_NAME + "\"," +
				"\"password\":\"" + TEST_USER_PASSWORD + "\"}";

		this.mockMvc.perform(post("/members")
				.content(inputJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andDo(print())
				.andDo(MemberDocumentation.failToCreateMember());
	}

	@Test
	public void failToCreateDuplicatedMember() throws Exception {

	}

	@Test
	public void readMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
		given(jwtTokenProvider.validateToken(any())).willReturn(true);
		given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("loginMemberEmail", TEST_USER_EMAIL);

		this.mockMvc.perform(get("/members?email=" + TEST_USER_EMAIL)
				.session(session)
				.header("authorization", "Bearer " + TEST_USER_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(TEST_USER_NAME))
				.andDo(print())
				.andDo(MemberDocumentation.readMember());
	}

	@Test
	public void failToReadMemberOfEmail() throws Exception {
		given(memberService.findMemberByEmail(any())).willThrow(new NoMemberExistException());
		given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
		given(jwtTokenProvider.validateToken(any())).willReturn(true);
		given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("loginMemberEmail", TEST_USER_EMAIL);

		this.mockMvc.perform(get("/members?email=" + "이상한 이메일")
				.session(session)
				.header("authorization", "Bearer " + TEST_USER_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print())
				.andDo(MemberDocumentation.failToReadMemberOfEmail());
	}

	@Test
	public void updateMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		doNothing().when(memberService).updateMember(any(), any());
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
		given(jwtTokenProvider.validateToken(any())).willReturn(true);
		given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("loginMemberEmail", TEST_USER_EMAIL);

		String inputJson = "{" +
				"\"name\":\"" + TEST_USER_NAME + "\"," +
				"\"password\":\"" + TEST_USER_PASSWORD + "\"}";

		this.mockMvc.perform(put("/members/{id}", 1L)
				.session(session)
				.header("authorization", "Bearer " + TEST_USER_TOKEN)
				.content(inputJson)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(MemberDocumentation.updateMember());
	}

	@Test
	public void deleteMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		doNothing().when(memberService).deleteMember(any());
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
		given(jwtTokenProvider.validateToken(any())).willReturn(true);
		given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("loginMemberEmail", TEST_USER_EMAIL);

		this.mockMvc.perform(delete("/members/{id}", 1L)
				.session(session)
				.header("authorization", "Bearer " + TEST_USER_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(print())
				.andDo(MemberDocumentation.deleteMember());
	}

	@Test
	public void failToAuthorizeMemberBecauseByToken() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
		given(jwtTokenProvider.validateToken(any())).willReturn(false);
		given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("loginMemberEmail", TEST_USER_EMAIL);

		this.mockMvc.perform(get("/members?email=" + TEST_USER_EMAIL)
				.session(session)
				.header("authorization", "Bearer " + TEST_USER_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andDo(print())
				.andDo(MemberDocumentation.failToAuthorizeMemberByToken());
	}

	@Test
	public void failToAuthorizeMemberBecauseBySession() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		given(memberService.findMemberByEmail(any())).willReturn(member);
		given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
		given(jwtTokenProvider.validateToken(any())).willReturn(true);
		given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

		this.mockMvc.perform(get("/members?email=" + TEST_USER_EMAIL)
				.header("authorization", "Bearer " + TEST_USER_TOKEN)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andDo(print())
				.andDo(MemberDocumentation.failToAuthorizeMemberBySession());
	}
}
