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
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginMemberControllerTest {

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected MemberRepository memberRepository;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    protected MockMvc mockMvc;
    private Member member;
    private String token;
    private List<Favorite> favorites;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        favorites = Arrays.asList(new Favorite(1L, 1L, 2L), new Favorite(2L, 2L, 3L), new Favorite(3L, 3L, 4L));
        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD, new HashSet<>(favorites));
        token = jwtTokenProvider.createToken(member.getEmail());
    }

    @Test
    void login() throws Exception {
        given(memberService.createMember(any())).willReturn(member);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
                "\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/oauth/token")
                .header("Authorization", "bearer " + token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.login());
    }

    @Test
    void updateMember() throws Exception {
        String inputJson = "{\"name\":\"" + "sample_name" + "\"," +
                "\"password\":\"" + "sample_password" + "\"}";

        this.mockMvc.perform(put("/me")
                .header("Authorization", "bearer " + token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }

    @Test
    void deleteMember() throws Exception {
        this.mockMvc.perform(delete("/me")
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());
    }

    @Test
    void createFavorite() throws Exception {
        String inputJson = "{\"source\":\"" + "1" + "\"," +
                "\"target\":\"" + "3" + "\"}";

        this.mockMvc.perform(post("/me/favorites")
                .header("Authorization", "bearer " + token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createFavorite());
    }

    @Test
    void findAllFavorites() throws Exception {
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(favorite -> new FavoriteResponse(favorite.getId(), favorite.getSourceStationId(), favorite.getTargetStationId()
                        , "강남", "압구정"))
                .collect(Collectors.toList());

        when(memberService.findAllFavorites(any())).thenReturn(favoriteResponses);

        this.mockMvc.perform(get("/me/favorites")
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.findAllFavorites());

    }

    @Test
    void removeFavorite() throws Exception {
        this.mockMvc.perform(delete("/me/favorites/1")
                .header("Authorization", "bearer " + token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.removeFavorite());
    }
}