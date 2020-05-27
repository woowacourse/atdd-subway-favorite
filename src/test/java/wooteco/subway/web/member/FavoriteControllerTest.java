package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.FavoriteService;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.service.member.MemberServiceTest.*;

@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {
    private final String TEST_SOURCE_NAME = "잠실역";
    private final String TEST_DESTINATION_NAME = "양재역";

    @MockBean
    protected FavoriteService favoriteService;

    @MockBean
    protected MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    @Value("${security.jwt.token.secret-key}")
    protected String secretKey;

    @Value("${security.jwt.token.expire-length}")
    protected Long validityInMilliseconds;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void create() throws Exception {
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));

        String inputJson = "{\"sourceName\":\"" + TEST_SOURCE_NAME + "\"," +
                "\"destinationName\":\"" + TEST_DESTINATION_NAME + "\"}";

        this.mockMvc.perform(post("/favorites")
                .header("Authorization", "bearer " + memberService.createToken(request))
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FavoriteDocumentation.createFavorite());
    }

    @Test
    public void read() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));
        List<FavoriteResponse> favorites = new ArrayList<>();
        favorites.add(new FavoriteResponse(TEST_SOURCE_NAME, TEST_DESTINATION_NAME));
        given(favoriteService.showAllFavorites(any())).willReturn(favorites);

        this.mockMvc.perform(get("/favorites")
                .header("Authorization", "bearer " + memberService.createToken(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FavoriteDocumentation.readFavorite());
    }

    @Test
    public void remove() throws Exception {
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));

        String inputJson = "{\"sourceName\":\"" + TEST_SOURCE_NAME + "\"," +
                "\"destinationName\":\"" + TEST_DESTINATION_NAME + "\"}";

        this.mockMvc.perform(delete("/favorites")
                .content(inputJson)
                .header("Authorization", "bearer " + memberService.createToken(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FavoriteDocumentation.removeFavorite());
    }

    @Test
    public void ifExists() throws Exception {
        LoginRequest request = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        given(memberService.createToken(any())).willReturn(new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL));
        given(favoriteService.ifFavoriteExist(any(), any(), any())).willReturn(true);

        this.mockMvc.perform(get("/favorites/exists?source=양재역&destination=잠실역")
                .header("Authorization", "bearer " + memberService.createToken(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(FavoriteDocumentation.ifExists());
    }
}
