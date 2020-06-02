package wooteco.subway.web.favorite;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.acceptance.AcceptanceTest.*;

import javax.servlet.http.Cookie;

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

import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.web.exception.DuplicatedFavoriteException;
import wooteco.subway.web.service.favorite.FavoriteService;
import wooteco.subway.web.service.favorite.dto.FavoriteResponse;
import wooteco.subway.web.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    private Member member;
    private Favorite favorite;
    private Cookie cookie;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();

        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        favorite = Favorite.of(1L, 1L, 1L, 2L);
        cookie = new Cookie("token", "dundung");
    }

    @Test
    public void addFavorite() throws Exception {
        given(memberService.save(any())).willReturn(member);
        given(favoriteService.create(any(), any())).willReturn(FavoriteResponse.of(favorite));

        String inputJson = "{\"sourceId\":\"" + 1L + "\"," +
            "\"targetId\":\"" + 2L + "\"}";

        this.mockMvc.perform(post("/favorites")
            .cookie(cookie)
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(FavoriteDocumentation.addFavorite());
    }

    @Test
    public void addFavoriteWithNoToken() throws Exception {
        given(memberService.save(any())).willReturn(member);
        given(favoriteService.create(any(), any())).willReturn(FavoriteResponse.of(favorite));

        String inputJson = "{\"sourceId\":\"" + 1L + "\"," +
            "\"targetId\":\"" + 2L + "\"}";

        this.mockMvc.perform(post("/favorites")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print())
            .andDo(FavoriteDocumentation.addFavoriteFail("favorites/create-no-login"));
    }

    @Test
    public void addDuplicatedFavorite() throws Exception {
        given(favoriteService.create(any(), any())).willThrow(
            new DuplicatedFavoriteException(1L, 2L));

        String inputJson = "{\"sourceId\":\"" + 1L + "\"," +
            "\"targetId\":\"" + 2L + "\"}";

        this.mockMvc.perform(post("/favorites")
            .cookie(cookie)
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(FavoriteDocumentation.addFavoriteFail("favorites/create-duplicated"));
    }

    @Test
    void deleteFavorite() throws Exception {
        given(memberService.save(member)).willReturn(member);

        this.mockMvc.perform(delete("/favorites/1")
            .cookie(cookie)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(FavoriteDocumentation.deleteFavorite());
    }

    @Test
    void deleteFavoriteWithNoToken() throws Exception {
        given(memberService.save(member)).willReturn(member);

        this.mockMvc.perform(delete("/favorites/1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized())
            .andDo(print())
            .andDo(FavoriteDocumentation.deleteFavoriteFail("favorites/delete-not-login"));
    }
}
