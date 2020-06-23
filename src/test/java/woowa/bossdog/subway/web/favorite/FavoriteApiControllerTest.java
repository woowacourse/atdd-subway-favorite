package woowa.bossdog.subway.web.favorite;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import woowa.bossdog.subway.domain.Favorite;
import woowa.bossdog.subway.domain.Member;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.infra.JwtTokenProvider;
import woowa.bossdog.subway.service.member.MemberService;
import woowa.bossdog.subway.service.favorite.FavoriteService;
import woowa.bossdog.subway.service.favorite.dto.FavoriteResponse;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteApiControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private WebApplicationContext ctx;

    @MockBean private FavoriteService favoriteService;
    @MockBean private MemberService memberService;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        member = new Member(111L, "test@test.com", "bossdog", "test");

        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(member.getEmail());
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void createFavorite() throws Exception {
        // given
        Station sourceStation = new Station(3L, "강남역");
        Station targetStation = new Station(4L, "서울역");
        FavoriteResponse response = FavoriteResponse.from(new Favorite(member, sourceStation, targetStation));

        given(favoriteService.createFavorite(any(), any())).willReturn(response);

        // when
        mvc.perform(post("/favorites")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN")
                .content("{\"source\":3,\"target\":4}"))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/favorites/" + response.getId()));

        // then
        verify(favoriteService).createFavorite(any(), any());
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void listFavorites() throws Exception {
        // given
        final List<FavoriteResponse> favoriteResponses = Lists.newArrayList(
                FavoriteResponse.from(new Favorite(10L, member, new Station(3L, "강남역"), new Station(4L, "서울역"))),
                FavoriteResponse.from(new Favorite(11L, member, new Station(8L, "신촌역"), new Station(21L, "사당역")))
        );
        given(favoriteService.listFavorites(any())).willReturn(favoriteResponses);

        // when
        mvc.perform(get("/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":10,\"memberId\":111,\"source\":{\"id\":3,\"name\":\"강남역\"},"+
                        "\"target\":{\"id\":4,\"name\":\"서울역\"}},{\"id\":11,\"memberId\":111,\"source\":{\"id\":8,\"name\":\"신촌역\"},"+
                        "\"target\":{\"id\":21,\"name\":\"사당역\"}}]"));

        // then
        verify(favoriteService).listFavorites(any());
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() throws Exception {
        // given
        Favorite favorite = new Favorite(10L, member, new Station(3L, "강남역"), new Station(4L, "사당역"));

        // when
        mvc.perform(delete("/favorites/" + favorite.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "ACCESS_TOKEN"))
                .andExpect(status().isNoContent());

        // then
        verify(favoriteService).deleteFavorite(any(), any());
    }
}