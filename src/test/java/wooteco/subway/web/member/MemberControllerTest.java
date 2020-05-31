package wooteco.subway.web.member;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @MockBean
    protected MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    private Member member;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        member = new Member(TEST_USER_ID, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void createMember() throws Exception {
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
    void createMember_Email_Exception() throws Exception {
        String inputJson = "{\"email\":\"" + TEST_INVALID_USER_EMAIL + "\"," +
                "\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void createMember_Blank_Exception() throws Exception {
        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
                "\"name\":\"" + TEST_INVALID_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void findMemberByEmail() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);

        this.mockMvc.perform(get("/members")
                .param("email", TEST_USER_EMAIL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.getMemberByEmail());
    }

    @Test
    void updateMember() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        String inputJson =
                "{\"name\":\"" + "NEW" + TEST_USER_NAME + "\"," +
                        "\"password\":\"" + "NEW" + TEST_USER_PASSWORD + "\"}";
        String token = jwtTokenProvider.createToken(TEST_USER_ID + ":" + TEST_USER_EMAIL);
        this.mockMvc.perform(put("/members/" + member.getId())
                .header("Authorization", "bearer " + token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }


    @Test
    void updateMember_Blank_Exception() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        String inputJson = "{\"name\":\"" + TEST_INVALID_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";
        String token = jwtTokenProvider.createToken(TEST_USER_ID + ":" + TEST_USER_EMAIL);
        this.mockMvc.perform(put("/members/" + member.getId())
                .header("Authorization", "bearer " + token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void deleteMember() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        String token = jwtTokenProvider.createToken(TEST_USER_ID + ":" + TEST_USER_EMAIL);
        this.mockMvc.perform(delete("/members/" + member.getId())
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());
    }

    @Test
    void createFavorite() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        String token = jwtTokenProvider.createToken(TEST_USER_ID + ":" + TEST_USER_EMAIL);

        String inputJson = "{\"startStationId\":\"" + TEST_START_STATION_ID + "\"," +
                "\"endStationId\":\"" + TEST_END_STATION_ID + "\"}";

        this.mockMvc.perform(post("/members/" + member.getId() + "/favorite")
                .header("Authorization", "bearer " + token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createFavorite());
    }

    @Test
    void findFavorite() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        String token = jwtTokenProvider.createToken(TEST_USER_ID + ":" + TEST_USER_EMAIL);

        this.mockMvc.perform(get("/members/" + member.getId() + "/favorite")
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.getFavorite());
    }

    @Test
    void deleteFavorite() throws Exception {
        given(memberService.findMemberByEmail(any())).willReturn(member);
        String token = jwtTokenProvider.createToken(TEST_USER_ID + ":" + TEST_USER_EMAIL);

        this.mockMvc.perform(delete("/members/" + member.getId() + "/favorite/" + 1L)
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteFavorite());
    }
}
