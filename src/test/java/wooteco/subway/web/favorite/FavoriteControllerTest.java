package wooteco.subway.web.favorite;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteCreateRequest;
import wooteco.subway.service.favorite.dto.FavoriteDeleteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {
    @MockBean
    FavoriteService favoriteService;

    @MockBean
    MemberService memberService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;
    private Gson gson;

    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        gson = new Gson();
    }

    @DisplayName("즐겨찾기 추가 controller 테스트")
    @Test
    void createFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME,
                TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);

        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest("강남역", "잠실역");

        this.mockMvc.perform(post("/favorites/me")
                .content(gson.toJson(favoriteCreateRequest))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(FavoriteDocumentation.createFavorite());
    }

    @DisplayName("즐겨찾기 조회 controller 테스트")
    @Test
    void getFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME,
                TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        favoriteResponses.add(new FavoriteResponse("강남역", "잠실역"));

        given(favoriteService.find(any())).willReturn(favoriteResponses);

        this.mockMvc.perform(get("/favorites/me")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(gson.toJson(favoriteResponses)))
                .andDo(print())
                .andDo(FavoriteDocumentation.getFavorite());
    }

    @DisplayName("즐겨찾기 삭제 controller 테스트")
    @Test
    void deleteFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME,
                TEST_USER_PASSWORD);
        given(memberService.createMember(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);

        FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest("강남역", "잠실역");

        this.mockMvc.perform(delete("/favorites/me")
                .content(gson.toJson(favoriteDeleteRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FavoriteDocumentation.deleteFavorite());
    }
}
