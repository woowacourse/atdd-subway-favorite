package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.FavoriteController;
import wooteco.subway.web.member.login.BearerAuthInterceptor;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = FavoriteController.class)
public class FavoriteControllerTest {

    @MockBean
    private FavoriteService favoriteService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private BearerAuthInterceptor bearerAuthInterceptor;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("본인의 모든 즐겨찾기 목록을 요청했을때 OK 응답이 오는지 테스트")
    @Test
    void getFavoritesTest() throws Exception {

        given(memberService.findAllFavoriteResponses(any())).willReturn(Collections.emptyList());

        this.mockMvc.perform(get("/me/favorites")
                .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("본인의 계정에 즐겨찾기를 추가했을때 OK 응답이 오는지 테스트")
    @Test
    void addFavoriteTest() throws Exception {

        given(memberService.addFavorite(any(), any())).willReturn(new Member());

        String inputJson = "{\"sourceStationName\":\"" + STATION_NAME_KANGNAM + "\"," +
                "\"targetStationName\":\"" + STATION_NAME_DOGOK + "\"}";

        this.mockMvc.perform(post("/me/favorites")
                .header("Authorization", "Bearer " + TEST_TOKEN)
                .content(inputJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("즐겨찾기 id로 즐겨찾기를 삭제했을때 OK응답이 오는지 테스트")
    @Test
    void deleteFavoriteTest() throws Exception {

        doNothing().when(memberService).deleteMember(any());

        this.mockMvc.perform(delete("/me/favorites/1")
                .header("Authorization", "Bearer " + TEST_TOKEN))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
