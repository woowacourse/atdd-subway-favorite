package wooteco.subway.web.favorite;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.AcceptanceTest.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {

    private static final long JAMSIL_STATION = 1L;
    private static final long SEOKCHONGOBUN_STATION = 2L;
    @MockBean
    protected MemberService memberService;
    @MockBean
    protected FavoriteService favoriteService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    public void createFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Favorite favorite = Favorite.of(1L, JAMSIL_STATION, SEOKCHONGOBUN_STATION);

        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        given(favoriteService.createFavorite(any(), any())).willReturn(favorite);

        Map<String, String> favoriteRequest = new HashMap<>();
        favoriteRequest.put("source", "잠실");
        favoriteRequest.put("target", "석촌고분");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestDto = objectMapper.writeValueAsString(favoriteRequest);

        this.mockMvc.perform(post("/favorite/me")
            .header("authorization", "Bearer 1q2w3e4r")
            .content(requestDto)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(FavoriteDocumentation.createFavorite());
    }

    @Test
    public void getFavoriteResponse() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        Favorite favorite = Favorite.of(1L, 1L, JAMSIL_STATION, SEOKCHONGOBUN_STATION);
        FavoriteResponse favoriteResponse = new FavoriteResponse(1L, "잠실", "석촌고분");

        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        given(favoriteService.getFavoriteResponse(any())).willReturn(Arrays.asList(favoriteResponse));

        this.mockMvc.perform(get("/favorite/me")
            .header("authorization", "Bearer 1q2w3e4r")
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.getFavorite());
    }

    @Test
    public void deleteFavorite() throws Exception {
        Member member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);

        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        doNothing().when(favoriteService).deleteFavorite(anyLong(), anyLong());

        this.mockMvc.perform(delete("/favorite/me/{id}", 1L)
            .header("authorization", "Bearer 1q2w3e4r")
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.deleteFavorite());
    }
}
