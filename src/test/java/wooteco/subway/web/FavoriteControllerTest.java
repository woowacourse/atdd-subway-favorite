package wooteco.subway.web;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.AcceptanceTest.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    void setUp() {
        token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QHRlc3QuY29tIiwiaWF0IjoxNTkwMTIwNjYyLCJleHAiOjE1OTAxMjQyNjJ9.QNR7KJFc0CmQ2VHOAxBiVrvdM9klpRt7Oh7tvkzLxqY";
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
    }

    @Test
    public void createFavorite() throws Exception {
        Favorite favorite = new Favorite(1L, 1L, 1L, 2L);

        given(favoriteService.createFavorite(any(), any())).willReturn(FavoriteResponse.from(favorite));

        mockMvc.perform(post("/favorites")
            .header("Authorization", token)
            .content("{\"memberId\":1, \"sourceStationId\":1, \"targetStationId\":2}")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    void getFavorites() throws Exception {
        List<FavoriteResponse> favorites = new ArrayList<>();
        Favorite favorite = new Favorite(1L, 1L, 1L, 2L);
        favorites.add(FavoriteResponse.from(favorite));

        given(favoriteService.getFavorites(any())).willReturn(favorites);

        mockMvc.perform(get("/favorites")
            .header("Authorization", token))
            .andExpect(status().isOk());
    }
}