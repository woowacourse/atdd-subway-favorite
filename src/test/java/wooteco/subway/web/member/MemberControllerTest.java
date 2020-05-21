package wooteco.subway.web.member;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = MemberController.class)
public class MemberControllerTest {
    private static final Long TEST_ID = 1L;
    private static final String TEST_EMAIL = "abc@naver.com";
    private static final String TEST_NAME = "brown";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_TOKEN = "this.is.token";

    private Gson gson = new Gson();
    private Member member;

    @MockBean
    private BearerAuthInterceptor bearerAuthInterceptor;

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
        given(memberService.createMember(any())).willReturn(member);

        Map<String, String> params = new LinkedHashMap<>();
        params.put("email", TEST_EMAIL);
        params.put("name", TEST_NAME);
        params.put("password", TEST_PASSWORD);

        String request = gson.toJson(params);
        mockMvc.perform(post("/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createMember());
    }

    @Test
    void getMemberByEmail() throws Exception {
        given(memberService.findMemberByEmail(TEST_EMAIL)).willReturn(member);
        MemberResponse expected = MemberResponse.of(member);

        MvcResult result = mockMvc.perform(get("/members")
                .param("email", TEST_EMAIL))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(gson.toJson(expected));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody UpdateMemberRequest param) {
        memberService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @Test
    void updateMember() throws Exception {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("name", "updatedName");
        params.put("password", "updatedPassword");

        doNothing().when(memberService).updateMember(any(), any());

        String request = gson.toJson(params);
        mockMvc.perform(put("/members/{id}", TEST_ID)
                .header("Authorization", "Bearer " + TEST_TOKEN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }
}
