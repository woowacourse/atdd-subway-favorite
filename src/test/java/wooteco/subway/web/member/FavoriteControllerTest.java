package wooteco.subway.web.member;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.web.member.MemberControllerTest.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(controllers = FavoriteController.class)
@Import({AuthorizationExtractor.class, JwtTokenProvider.class})
public class FavoriteControllerTest {
    @MockBean
    protected MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private AuthorizationExtractor authorizationExtractor;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private Member member;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new CharacterEncodingFilter(ENCODING, true))
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .build();

        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        given(authorizationExtractor.extract(any(), anyString())).willReturn(TEST_USER_TOKEN);
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        given(memberService.findMemberByEmail(anyString())).willReturn(member);
    }

    @DisplayName("즐겨찾기 추가 테스트")
    @Test
    void addFavorite() throws Exception {
        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/favorites")
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andDo(FavoriteDocumentation.addFavorite());
    }

    @DisplayName("즐겨찾기 목록 조회 테스트")
    @Test
    void getAllFavorites() throws Exception {
        Station station1 = new Station(1L, "잠실역");
        Station station2 = new Station(2L, "석촌역");
        Favorite favorite = new Favorite(1L, station1.getId(), station2.getId());
        List<FavoriteResponse> favorites = Arrays.asList(
            new FavoriteResponse(favorite, station1, station2),
            new FavoriteResponse(favorite, station2, station1)
        );
        given(memberService.getAllFavorites(any())).willReturn(favorites);

        mockMvc.perform(get("/favorites")
            .header(AUTHORIZATION, TEST_USER_TOKEN)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(FavoriteDocumentation.getAllFavorites());
    }

    @DisplayName("즐겨찾기 삭제 테스트")
    @Test
    void removeFavorite() throws Exception {
        Favorite favorite = new Favorite(1L, 1L, 2L);
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/favorites/{id}", favorite.getId())
            .header(AUTHORIZATION, TEST_USER_TOKEN))
            .andExpect(status().isNoContent())
            .andDo(FavoriteDocumentation.removeFavorite());
    }
}
