package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
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
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.acceptance.member.MemberAcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class MemberControllerTest {

    @MockBean
    protected MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    private Member member;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
        this.member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
    }

    @Test
    public void createMember() throws Exception {
        MemberResponse response = MemberResponse.of(member);
        given(memberService.createMember(any())).willReturn(response);

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
    void login() throws Exception {
        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/login")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.loginMember());
    }
}
