package wooteco.subway.web.member;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.AcceptanceTest.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

	@InjectMocks
	protected MemberService memberService;
	@MockBean
	protected MemberRepository memberRepository;

	@MockBean
	protected JwtTokenProvider jwtTokenProvider;

	@Autowired
	protected MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
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
	public void login() throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("email", TEST_USER_EMAIL);
		params.put("password", TEST_USER_PASSWORD);
		String accessToken = "q1w2e3r4";

		when(memberService.createToken(any())).thenReturn(accessToken);

		ObjectMapper mapper = new ObjectMapper();
		String loginRequest = mapper.writeValueAsString(params);

		this.mockMvc.perform(post("/oauth/token")
			.content(loginRequest)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE)
		)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.login());
	}

	@Test
	public void getMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		when(jwtTokenProvider.validateToken(any())).thenReturn(true);
		when(jwtTokenProvider.getSubject(any())).thenReturn(TEST_USER_EMAIL);
		when(memberService.findMemberByEmail(any())).thenReturn(member);

		this.mockMvc.perform(get("/members/me")
			.header("authorization", "Bearer 1q2w3e4r")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.getMember());
	}

	@Test
	public void updateMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		Map<String, String> params = new HashMap<>();
		params.put("name", "NEW_" + TEST_USER_NAME);
		params.put("password", "NEW_" + TEST_USER_PASSWORD);

		ObjectMapper objectMapper = new ObjectMapper();
		String updateRequest = objectMapper.writeValueAsString(params);

		when(jwtTokenProvider.validateToken(any())).thenReturn(true);
		when(jwtTokenProvider.getSubject(any())).thenReturn(TEST_USER_EMAIL);
		when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
		when(memberRepository.findById(any())).thenReturn(Optional.of(member));
		
		this.mockMvc.perform(put("/members/me")
			.header("authorization", "Bearer 1q2w3e4r")
			.content(updateRequest)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.updateMember());
	}

	@Test
	public void deleteMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		when(jwtTokenProvider.validateToken(any())).thenReturn(true);
		when(jwtTokenProvider.getSubject(any())).thenReturn(TEST_USER_EMAIL);
		when(memberService.findMemberByEmail(any())).thenReturn(member);

		this.mockMvc.perform(delete("/members/me")
			.header("authorization", "Bearer 1q2w3e4r")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.deleteMember());
	}
}