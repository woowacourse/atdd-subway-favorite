package wooteco.subway.web.favorite;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.service.member.MemberServiceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();

        final Member member = new Member(63L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.getSubject(any())).thenReturn(TEST_USER_EMAIL);
        when(memberService.findMemberByEmail(any())).thenReturn(member);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    public void create() throws Exception {
        final FavoriteResponse favoriteResponse = new FavoriteResponse(10L, 63L, 1L, 3L);
        when(favoriteService.addFavorite(any(), any())).thenReturn(favoriteResponse);

        mvc.perform(post("/favorites")
                .header("Authorization", "Bearer mockToken")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"source\":1,\"target\":3}"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(FavoriteDocumentation.createFavorite());

        verify(favoriteService).addFavorite(eq(63L), any());
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    public void showAll() throws Exception {
        final List<FavoriteResponse> responses = new ArrayList<>();
        responses.add(new FavoriteResponse(
                10L, 63L, 1L, 3L, "GangNam", "JamSil"));
        when(favoriteService.showMyAllFavorites(any())).thenReturn(responses);

        mvc.perform(get("/favorites")
                .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":10,\"memberId\":63,\"sourceStationId\":1,\"targetStationId\":3," +
                        "\"sourceStationName\":\"GangNam\",\"targetStationName\":\"JamSil\"}]"))
                .andDo(print())
                .andDo(FavoriteDocumentation.getFavorite());
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void removeFavorites() throws Exception {
        Favorite favorite = new Favorite(10L, 63L, 1L, 3L);
        mvc.perform(delete("/favorites/" + favorite.getId())
                .header("Authorization", "Bearer mockToken"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(FavoriteDocumentation.deleteFavorite());

        verify(favoriteService).removeFavorite(eq(10L));
    }
}