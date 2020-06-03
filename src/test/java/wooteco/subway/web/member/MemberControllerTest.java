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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.DuplicateEmailException;
import wooteco.subway.exception.MemberNotFoundException;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;

@ExtendWith(RestDocumentationExtension.class)
@Import({JwtTokenProvider.class, AuthorizationExtractor.class})
@WebMvcTest(MemberController.class)
public class MemberControllerTest {
	private static final long MEMBER_ID = 1L;
	private static final String MEMBER_EMAIL = "tiger@luv.com";
	private static final String MEMBER_NAME = "테스트이름";
	private static final String MEMBER_PASSWORD = "prettiger";

	@MockBean
	private MemberService memberService;

	@Autowired
	private MockMvc mockMvc;

	private Member member;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new ShallowEtagHeaderFilter())
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.apply(documentationConfiguration(restDocumentation))
			.build();

		member = new Member(MEMBER_ID, MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
	}

	@DisplayName("회원등록을 성공적으로 마치면, Created 상태를 반환하는지 확인한다.")
	@Test
	void createMember() throws Exception {
		given(memberService.createMember(any())).willReturn(MemberResponse.of(member));

		String body = "{\"email\" : \"" + MEMBER_EMAIL + "\", \"name\" : \"" + MEMBER_NAME + "\", \"password\" : \""
			+ MEMBER_PASSWORD + "\"}";

		this.mockMvc.perform(post("/members")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andExpect(status().isCreated())
			.andDo(print())
			.andDo(MemberDocumentation.createMember());
	}

	@DisplayName("회원 이메일이 중복되는 경우, 등록을 성공적으로 마치지 못하며, BadRequest 상태를 반환한다.")
	@Test
	void createMemberWithDuplicateEmail() throws Exception {
		given(memberService.createMember(any())).willThrow(new DuplicateEmailException());

		String body = "{\"email\" : \"" + MEMBER_EMAIL + "\", \"name\" : \"" + MEMBER_NAME + "\", \"password\" : \""
			+ MEMBER_PASSWORD + "\"}";

		this.mockMvc.perform(post("/members")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(body))
			.andExpect(status().isBadRequest())
			.andDo(print())
			.andDo(MemberDocumentation.createMemberException());
	}

	@DisplayName("id에 해당하는 회원의 정보와 200 상태코드 반환")
	@Test
	void findMemberInfoById() throws Exception {
		given(memberService.findMemberById(MEMBER_ID)).willReturn(MemberResponse.of(member));
		String expected = "{\"email\" : \"" + MEMBER_EMAIL + "\", \"name\" : \"" + MEMBER_NAME + "\", \"id\" :"
			+ MEMBER_ID + "}";

		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/members/{id}", MEMBER_ID)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(expected))
			.andDo(print())
			.andDo(MemberDocumentation.getMember())
		;
	}

	@DisplayName("id에 해당하는 회원이 존재하지 않는 경우 404 상태코드 반환")
	@Test
	void findMemberInfoById_when_memberNotExist() throws Exception {
		given(memberService.findMemberById(MEMBER_ID)).willThrow(new MemberNotFoundException());

		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/members/{id}", MEMBER_ID)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andDo(print())
			.andDo(MemberDocumentation.getMemberException())
		;
	}
}
