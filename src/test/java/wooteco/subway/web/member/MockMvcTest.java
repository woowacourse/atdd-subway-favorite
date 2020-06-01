package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.member.info.AuthInfo;
import wooteco.subway.web.member.info.UriInfo;
import wooteco.subway.web.member.interceptor.AuthorizationExtractor;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class MockMvcTest {
    protected static final String TEST_USER_EMAIL = "brown@email.com";
    protected static final String TEST_USER_NAME = "브라운";
    protected static final String TEST_USER_PASSWORD = "brown";
    protected static final String TEST_USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1OTAwNTA1NjMsImV4cCI6MTU5MDA1NDE2M30.bPh4VZcEj7aYlXDBP_o-1IqZw5AoKCIetrHvI7OcB_k";
    protected static final MockHttpSession TEST_USER_SESSION = new MockHttpSession();

    static {
        TEST_USER_SESSION.setAttribute("loginMemberEmail", TEST_USER_EMAIL);
    }

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
    @MockBean
    protected AuthorizationExtractor authorizationExtractor;
    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter()).alwaysDo(print())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    protected ResultActions postAction(UriInfo uri, String inputJson, AuthInfo authInfo) throws Exception {
        return this.mockMvc.perform(post(uri.getUri(), uri.getPathVariables())
                .session(authInfo.getSession())
                .header("authorization", "Bearer " + authInfo.getToken())
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions getAction(UriInfo uri, String inputJson, AuthInfo authInfo) throws Exception {
        return this.mockMvc.perform(get(uri.getUri(), uri.getPathVariables())
                .session(authInfo.getSession())
                .header("authorization", "Bearer " + authInfo.getToken())
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions putAction(UriInfo uri, String inputJson, AuthInfo authInfo) throws Exception {
        return this.mockMvc.perform(put(uri.getUri(), uri.getPathVariables())
                .session(authInfo.getSession())
                .header("authorization", "Bearer " + authInfo.getToken())
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions deleteAction(UriInfo uri, String inputJson, AuthInfo authInfo) throws Exception {
        return this.mockMvc.perform(delete(uri.getUri(), uri.getPathVariables())
                .session(authInfo.getSession())
                .header("authorization", "Bearer " + authInfo.getToken())
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));
    }
}
