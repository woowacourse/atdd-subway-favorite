package wooteco.subway.web.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerIntegrationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    MemberService memberService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("내 정보 확인에서 이메일이 맞지 않을경우 익셉션이 발생한다")
    @Test
    void nonMatchEmailInGetMyInfoTest() throws Exception {
        String anotherEmail = "anotherEmail@gmail.com";

        String token = jwtTokenProvider.createToken(anotherEmail);

        String uri = "/auth/members";

        mockMvc.perform(get(uri)
                .header("authorization", "Bearer" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("유저 정보 수정 시 이메일이 맞지 않을경우 익셉션이 발생한다")
    @Test
    void unAuthorizationUpdateRequestTest() throws Exception {
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("coyle", "6315");

        memberService.createMember(new Member(1L, "email@gmail.com", "ramen", "6315"));

        String anotherEmail = "anotherEmail@gmail.com";
        String wrongToken = jwtTokenProvider.createToken(anotherEmail);
        String updateData = OBJECT_MAPPER.writeValueAsString(updateMemberRequest);

        String uri = "/auth/members";

        mockMvc.perform(put(uri)
                .header("Authorization", "Bearer" + wrongToken)
                .content(updateData)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                //todo
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("유저 정보 삭제가 성공한다")
    @Test
    void deleteMemberDateSuccessTest() throws Exception {
        Member member = memberService.createMember(new Member("email@gmail.com", "ramen", "6315"));

        String token = jwtTokenProvider.createToken(member.getEmail());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/auth/members")
                .header("Authorization", "Bearer" + token))
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember())
                .andExpect(status().isNoContent());
    }

    @DisplayName("토큰 값이 없을 경우 익셉션이 발생한다")
    @Test
    void deleteMemberDateFailTest() throws Exception {
        String uri = "/auth/members";

        mockMvc.perform(delete(uri))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("삭제 하려는 id가 일치 하지 않을 경우 익셉션이 발생한다")
    @Test
    void deleteMemberDateFailTest2() throws Exception {
        String wrongMemberEmailToken = jwtTokenProvider.createToken("theWrongEmail@gmail.com");

        String uri = "/auth/members";

        mockMvc.perform(delete(uri)
                .header("Authorization", "Bearer " + wrongMemberEmailToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
