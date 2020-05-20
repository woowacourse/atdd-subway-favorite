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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.TokenResponse;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

	@MockBean
	protected MemberService memberService;

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
	public void getMember() throws Exception {
		Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
		given(memberService.createMember(any())).willReturn(member);
		String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
			"\"name\":\"" + TEST_USER_NAME + "\"," +
			"\"password\":\"" + TEST_USER_PASSWORD + "\"}";
		TokenResponse login = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);

		this.mockMvc.perform(get("/members")
			.header("authorization", login.getTokenType() + " " + login.getAccessToken())
			.content(inputJson)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.getMember());
	}

	private TokenResponse login(String email, String password) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		params.put("password", password);

		ObjectMapper mapper = new ObjectMapper();
		String s = mapper.writeValueAsString(params);

		MvcResult mvcResult = this.mockMvc.perform(post("/oauth/token")
			.content(s)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		return mapper.readValue(contentAsString, TokenResponse.class);
	}
}