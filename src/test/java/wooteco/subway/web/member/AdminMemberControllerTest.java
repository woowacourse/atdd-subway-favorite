package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.BearerAuthInterceptorTest.*;
import static wooteco.subway.acceptance.AcceptanceTest.*;
import static wooteco.subway.web.AuthorizationExtractor.*;
import static wooteco.subway.web.BearerAuthInterceptor.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import wooteco.subway.docs.AdminMemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public class AdminMemberControllerTest {

	private static final Gson GSON = new Gson();

	private Member member;

	@MockBean
	private MemberService memberService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
		                              .addFilter(new ShallowEtagHeaderFilter())
		                              .addFilters(new CharacterEncodingFilter("UTF-8", true))
		                              .alwaysDo(print())
		                              .apply(documentationConfiguration(restDocumentation))
		                              .build();
	}

	@Test
	void createMember() throws Exception {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("email", TEST_USER_EMAIL);
		params.put("name", TEST_USER_NAME);
		params.put("password", TEST_USER_PASSWORD);

		given(memberService.createMember(any())).willReturn(member);

		mockMvc.perform(post("/admin/members").accept(MediaType.APPLICATION_JSON)
		                                      .contentType(MediaType.APPLICATION_JSON)
		                                      .content(GSON.toJson(params)))
		       .andExpect(status().isCreated())
		       .andDo(AdminMemberDocumentation.createMember());
	}

	@Test
	void getMemberByEmail() throws Exception {
		given(memberService.findMemberByEmail(TEST_USER_EMAIL)).willReturn(member);

		mockMvc.perform(get("/admin/members")
			.param("email", TEST_USER_EMAIL))
		       .andExpect(status().isOk())
		       .andExpect(jsonPath("$.email", Matchers.is(TEST_USER_EMAIL)))
		       .andExpect(jsonPath("$.name", Matchers.is(TEST_USER_NAME)))
		       .andDo(AdminMemberDocumentation.getMemberByEmail());
	}

	@Test
	void updateMember() throws Exception {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("name", TEST_UPDATE_DELIMITER + TEST_USER_NAME);
		params.put("password", TEST_UPDATE_DELIMITER + TEST_USER_PASSWORD);

		doNothing().when(memberService).updateMember(anyLong(), any());

		mockMvc.perform(put("/admin/members/{id}", TEST_USER_ID)
			.header(AUTHORIZATION, BEARER_TOKEN + TEST_TOKEN_SECRET_KEY)
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(GSON.toJson(params)))
		       .andExpect(status().isOk())
		       .andDo(AdminMemberDocumentation.updateMember());
	}

	@Test
	void deleteMember() throws Exception {
		mockMvc.perform(delete("/admin/members/{id}", TEST_USER_ID))
		       .andExpect(status().isNoContent())
		       .andDo(AdminMemberDocumentation.deleteMember());
	}

}
