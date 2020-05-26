package wooteco.subway.web.member;

import com.google.gson.Gson;
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
import wooteco.subway.docs.AdminMemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
public class AdminMemberControllerTest {
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
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
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

        mockMvc.perform(post("/admin/members").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(params)))
                .andExpect(status().isCreated())
                .andDo(AdminMemberDocumentation.createMember());
    }

    @Test
    void getMemberByEmail() throws Exception {
        given(memberService.findMemberByEmail(TEST_EMAIL)).willReturn(member);

        mockMvc.perform(get("/admin/members")
                .param("email", TEST_EMAIL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is(TEST_EMAIL)))
                .andExpect(jsonPath("$.name", Matchers.is(TEST_NAME)))
                .andDo(AdminMemberDocumentation.getMemberByEmail());
    }

    @Test
    void updateMember() throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", "updatedName");
        params.put("password", "updatedPassword");

        doNothing().when(memberService).updateMember(anyLong(), any());

        mockMvc.perform(put("/admin/members/{id}", TEST_ID)
                .header("Authorization", "Bearer " + TEST_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(params)))
                .andExpect(status().isOk())
                .andDo(AdminMemberDocumentation.updateMember());
    }

    @Test
    void deleteMember() throws Exception {
        mockMvc.perform(delete("/admin/members/{id}", TEST_ID))
                .andExpect(status().isNoContent())
                .andDo(AdminMemberDocumentation.deleteMember());
    }
}
