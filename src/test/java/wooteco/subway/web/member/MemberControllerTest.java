package wooteco.subway.web.member;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.EntityNotFoundException;
import wooteco.subway.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
	private static final long TIGER_ID = 1L;
	private static final String TIGER_EMAIL = "tiger@luv.com";
	private static final String TIGER_NAME = "tiger";
	private static final String TIGER_PASSWORD = "prettiger";

	@MockBean
	MemberService memberService;

	@Autowired
	MockMvc mockMvc;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			// TODO: 2020/05/19 캐시 필터는 webApplicationContext에 이미 들어있는데 필터를 또 추가하는 이유는요?
			//	.addFilter(new ShallowEtagHeaderFilter())
			.apply(documentationConfiguration(restDocumentation))
			.build();

		System.out.println(webApplicationContext.containsBean("shallowEtagHeaderFilter"));
	}

	@DisplayName("회원등록을 성공적으로 마치면, Created 상태를 반환하는지 확인한다.")
	@Test
	void createMember() throws Exception {
		Member member = new Member(TIGER_ID, TIGER_EMAIL, TIGER_NAME, TIGER_PASSWORD);
		given(memberService.createMember(any())).willReturn(member);

		String body = "{\"email\" : \"" + TIGER_EMAIL + "\", \"name\" : \"" + TIGER_NAME + "\", \"password\" : \""
			+ TIGER_PASSWORD + "\"}";

		this.mockMvc.perform(post("/members")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andExpect(status().isCreated())
			.andDo(print())
			.andDo(MemberDocumentation.createMember());
	}

	@DisplayName("이메일로 회원을 조회하고, OK 상태코드를 반환하고, 해당 회원의 정보를 반환하는지 확인한다.")
	@Test
	void getMemberByEmail() throws Exception {
		Member member = new Member(TIGER_ID, TIGER_EMAIL, TIGER_NAME, TIGER_PASSWORD);
		given(memberService.findMemberByEmail(TIGER_EMAIL)).willReturn(member);

		String expected =
			"{\"email\" : \"" + TIGER_EMAIL + "\", \"name\" : \"" + TIGER_NAME + "\", \"id\" : " + TIGER_ID + "}";

		this.mockMvc.perform(get("/members")
			.param("email", TIGER_EMAIL)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expected))
			.andDo(print())
			.andDo(MemberDocumentation.getMember());
	}

	@DisplayName("이메일로 회원을 조회하는데, 회원이 없는 경우 404 Not Found를 반환한다.")
	@Test
	void getMemberByEmail_notExistEmail() throws Exception {
		given(memberService.findMemberByEmail(TIGER_EMAIL)).willThrow(
			new EntityNotFoundException(TIGER_EMAIL + "에 해당하는 회원이 없습니다."));

		this.mockMvc.perform(get("/members")
			.param("email", TIGER_EMAIL)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(print())
			.andDo(MemberDocumentation.getMemberException());
	}

	@DisplayName("아이디로 정보를 갱신할 회원을 지정하여 정보를 갱신한 후, OK 상태코드를 반환한다.")
	@Test
	void updateMember() throws Exception {
		String body = "{\"name\" : \"" + TIGER_NAME + "\", \"password\" : \"" + TIGER_PASSWORD + "\"}";

		this.mockMvc.perform(RestDocumentationRequestBuilders.put("/members/{id}", TIGER_ID)
			.content(body)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(print())
			.andDo(MemberDocumentation.updateMember())
		;
	}

	@DisplayName("아이디로 회원 정보를 삭제한 후, NoContent 상태코드를 반환한다.")
	@Test
	void deleteMember() throws Exception {

		this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/members/{id}", TIGER_ID)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andDo(print())
			.andDo(MemberDocumentation.deleteMember());
	}
}
