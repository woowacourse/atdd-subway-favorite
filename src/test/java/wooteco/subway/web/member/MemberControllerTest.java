package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.google.gson.Gson;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public class MemberControllerTest {
	private static final Long TEST_ID = 1L;
	private static final String TEST_EMAIL = "abc@naver.com";
	private static final String TEST_NAME = "brown";
	private static final String TEST_PASSWORD = "password";
	private static final String TEST_TOKEN = "this.is.token";

	private Gson gson = new Gson();
	private Member member;

	@MockBean
	private MemberService memberService;

	private MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.member = new Member(TEST_ID, TEST_EMAIL, TEST_NAME, TEST_PASSWORD);

		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
		                              .addFilter(new ShallowEtagHeaderFilter())
		                              .apply(documentationConfiguration(restDocumentation))
		                              .build();
	}

	@Test
	void createMember() throws Exception {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("email", TEST_EMAIL);
		params.put("name", TEST_NAME);
		params.put("password", TEST_PASSWORD);

		given(memberService.createMember(any())).willReturn(member);

		mockMvc.perform(post("/members").accept(MediaType.APPLICATION_JSON)
		                                .contentType(MediaType.APPLICATION_JSON)
		                                .content(gson.toJson(params)))
		       .andExpect(status().isCreated())
		       .andDo(print())
		       .andDo(MemberDocumentation.createMember());
	}

	@Test
	void getMemberByEmail() throws Exception {
		given(memberService.findMemberByEmail(TEST_EMAIL)).willReturn(member);

		MemberResponse expected = MemberResponse.of(member);

		mockMvc.perform(get("/members").param("email", TEST_EMAIL))
		       .andExpect(status().isOk())
		       .andDo(print())
		       .andExpect(jsonPath("$.email", Matchers.is(TEST_EMAIL)))
		       .andExpect(jsonPath("$.name", Matchers.is(TEST_NAME)));
	}

	@Test
	void updateMember() throws Exception {
		Map<String, String> params = new LinkedHashMap<>();
		params.put("name", "updatedName");
		params.put("password", "updatedPassword");

		doNothing().when(memberService).updateMember(any(), any());

		mockMvc.perform(put("/members/{id}", TEST_ID).header("Authorization", "Bearer " + TEST_TOKEN)
		                                             .accept(MediaType.APPLICATION_JSON)
		                                             .contentType(MediaType.APPLICATION_JSON)
		                                             .content(gson.toJson(params)))
		       .andExpect(status().isOk())
		       .andDo(print())
		       .andDo(MemberDocumentation.updateMember());
	}

	@Test
	void deleteMember() throws Exception {
		mockMvc.perform(delete("/members/{id}", TEST_ID))
		       .andExpect(status().isNoContent())
		       .andDo(print());
	}
}
