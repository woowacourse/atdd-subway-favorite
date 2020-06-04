package wooteco.subway.web;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.TEST_USER_EMAIL;
import static wooteco.subway.AcceptanceTest.TEST_USER_NAME;
import static wooteco.subway.AcceptanceTest.TEST_USER_PASSWORD;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Sql("/truncate.sql")
public class ControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	protected MemberService memberService;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private String mockToken;
	protected ObjectMapper objectMapper = new ObjectMapper();
	protected Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext,
		RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(print())
			.build();
		mockToken = "bearer" + jwtTokenProvider.createToken(TEST_USER_EMAIL);
	}

	protected void post(String path, String request, ResultMatcher status,
		RestDocumentationResultHandler documentationResultHandler) throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.post(path)
			.content(request)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status)
			.andDo(print())
			.andDo(documentationResultHandler);
	}

	protected void postWithAuth(String path, String request, ResultMatcher status,
		RestDocumentationResultHandler documentationResultHandler) throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.post(path)
			.header(HttpHeaders.AUTHORIZATION, mockToken)
			.content(request)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status)
			.andDo(print())
			.andDo(documentationResultHandler);
	}

	protected MvcResult getWithAuth(String path, RestDocumentationResultHandler documentationResultHandler) throws
		Exception {
		return this.mockMvc.perform(RestDocumentationRequestBuilders.get(path)
			.header(HttpHeaders.AUTHORIZATION, mockToken))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(documentationResultHandler)
			.andReturn();
	}

	protected void putWithAuth(String path, String request, ResultMatcher result) throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.put(path)
			.header(HttpHeaders.AUTHORIZATION, mockToken)
			.content(request)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(result)
			.andDo(print())
			.andDo(MemberDocumentation.updateMember());
	}

	protected void putWithAuthThenFail(String path, String request) throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.put(path)
			.header(HttpHeaders.AUTHORIZATION, mockToken)
			.content(request)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(MemberDocumentation.failedUpdateMemberByBlank());
	}

	protected void deleteWithAuth(String path, RestDocumentationResultHandler documentationResultHandler) throws
		Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.delete(path)
			.header(HttpHeaders.AUTHORIZATION, mockToken))
			.andExpect(status().isNoContent())
			.andDo(print())
			.andDo(documentationResultHandler);
	}
}
