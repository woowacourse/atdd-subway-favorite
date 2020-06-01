package wooteco.subway.web.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationExtension;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.exception.DuplicatedEmailException;
import wooteco.subway.exception.NoMemberExistException;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.info.AuthInfo;
import wooteco.subway.web.member.info.UriInfo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
public class MemberControllerTest extends MockMvcTest{
    private static final String EMAIL_NAME_PASSWORD_FORMAT =  "{\"email\":\"%s\",\"name\":\"%s\",\"password\":\"%s\"}";
    private static final String NAME_PASSWORD_FORMAT =  "{\"name\":\"%s\",\"password\":\"%s\"}";
    private static final String WEIRED_USER_EMAIL = "이상한이메일";

    @MockBean
    protected MemberService memberService;

    @DisplayName("유저 생성 컨트롤러")
    @Test
    public void createMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);

        UriInfo uriInfo = UriInfo.of("/members");
        String inputJson = String.format(EMAIL_NAME_PASSWORD_FORMAT, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        postAction(uriInfo, inputJson, AuthInfo.empty())
                .andExpect(status().isCreated())
                .andDo(MemberDocumentation.createMember());
    }

    @DisplayName("유저 생성 실패(이메일 형식) 컨트롤러")
    @Test
    public void failToCreateWeiredEmailMember() throws Exception {
        UriInfo uriInfo = UriInfo.of("/members");
        String inputJson = String.format(EMAIL_NAME_PASSWORD_FORMAT, WEIRED_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        postAction(uriInfo, inputJson, AuthInfo.empty())
                .andExpect(status().isBadRequest())
                .andDo(MemberDocumentation.failToCreateMemberByEmail());
    }

    @DisplayName("유저 생성 실패(중복 이메일) 컨트롤러")
    @Test
    public void failToCreateDuplicatedMember() throws Exception {
        given(memberService.createMember(any())).willThrow(new DuplicatedEmailException());

        UriInfo uriInfo = UriInfo.of("/members");
        String inputJson = String.format(EMAIL_NAME_PASSWORD_FORMAT, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        postAction(uriInfo, inputJson, AuthInfo.empty())
                .andExpect(status().isBadRequest())
                .andDo(MemberDocumentation.failToCreateMemberByDuplication());
    }

    @DisplayName("유저 정보 읽기 컨트롤러")
    @Test
    public void readMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members?email=" + TEST_USER_EMAIL);
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, TEST_USER_SESSION);

        getAction(uriInfo, "", authInfo)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TEST_USER_NAME))
                .andDo(MemberDocumentation.readMember());
    }

    @DisplayName("유저 정보 읽기 실패(잘못된 이메일) 컨트롤러")
    @Test
    public void failToReadMemberOfWrongEmail() throws Exception {
        given(memberService.findMemberByEmail(any())).willThrow(new NoMemberExistException());
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members?email=" + WEIRED_USER_EMAIL);
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, TEST_USER_SESSION);

        getAction(uriInfo, "", authInfo)
                .andExpect(status().isNotFound())
                .andDo(print())
                .andDo(MemberDocumentation.failToReadMemberOfEmail());
    }

    @DisplayName("유저 정보 수정 컨트롤러")
    @Test
    public void updateMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        doNothing().when(memberService).updateMember(any(), any());
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members/{id}", new Long[] {1L});
        String inputJson = String.format(NAME_PASSWORD_FORMAT, TEST_USER_NAME, TEST_USER_PASSWORD);
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, TEST_USER_SESSION);

        putAction(uriInfo, inputJson, authInfo)
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }

    @DisplayName("유저 정보 삭제 컨트롤러")
    @Test
    public void deleteMember() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        doNothing().when(memberService).deleteMember(any());
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members/{id}", new Long[] {1L});
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, TEST_USER_SESSION);

        deleteAction(uriInfo, "", authInfo)
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());
    }

    @DisplayName("유저 토큰 인증 실패 컨트롤러")
    @Test
    public void failToAuthorizeMemberBecauseOfToken() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(false);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members?email=" + TEST_USER_EMAIL);
        AuthInfo authInfo = AuthInfo.of("", TEST_USER_SESSION);

        getAction(uriInfo, "", authInfo)
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(MemberDocumentation.failToAuthorizeMemberByToken());
    }

    @DisplayName("유저 세션 인증 실패 컨트롤러")
    @Test
    public void failToAuthorizeMemberBecauseOfSession() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(authorizationExtractor.extract(any(), any())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        UriInfo uriInfo = UriInfo.of("/members?email=" + TEST_USER_EMAIL);
        AuthInfo authInfo = AuthInfo.of(TEST_USER_TOKEN, new MockHttpSession());

        getAction(uriInfo, "", authInfo)
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(MemberDocumentation.failToAuthorizeMemberBySession());
    }
}
