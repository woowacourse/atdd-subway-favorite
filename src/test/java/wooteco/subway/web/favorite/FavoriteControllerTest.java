package wooteco.subway.web.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.service.member.MemberService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    FavoriteService favoriteService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("즐겨찾기 추가한다.")
    @Test
    void createFavoriteTest() throws Exception {
        String sourceStation = "강남역";
        String targetStation = "한티역";

        Member member = new Member(DummyTestUserInfo.EMAIL, DummyTestUserInfo.NAME, DummyTestUserInfo.PASSWORD);
        CreateFavoriteRequest favoriteRequest = new CreateFavoriteRequest(sourceStation, targetStation);

        given(favoriteService.createFavorite(any(), any())).willReturn(new FavoriteResponse(1L, sourceStation, targetStation));
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(DummyTestUserInfo.EMAIL);
        given(memberService.findMemberByEmail(any())).willReturn(member);

        String uri = "/auth/favorites";
        String content = OBJECT_MAPPER.writeValueAsString(favoriteRequest);

        mockMvc.perform(post(uri)
                .header("Authorization", "Bearer " + "이메일 Token")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andDo(FavoriteDocumentation.create())
                .andExpect(status().isCreated());
    }

    @DisplayName("즐겨찾기 조회한다")
    @Test
    void selectFavoriteTest() throws Exception {
        FavoriteResponse favoriteResponse = new FavoriteResponse(1L, "강남역", "역삼");
        List<FavoriteResponse> favoriteResponses = Arrays.asList(favoriteResponse, favoriteResponse);
        Member member = new Member("email@gmail.com", null, null);

        given(favoriteService.findAllByMemberId(any()))
                .willReturn(new FavoritesResponse(favoriteResponses));
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);

        String uri = "/auth/favorites";

        MvcResult mvcResult = mockMvc.perform(get(uri)
                .header("Authorization", "Bearer " + "이메일 Token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        FavoritesResponse favoritesResponse =
                OBJECT_MAPPER.readValue(mvcResult.getResponse().getContentAsString(), FavoritesResponse.class);
        assertThat(favoritesResponse.getFavoriteResponses()).hasSize(2);
    }

    @DisplayName("즐겨찾기를 삭제 한다.")
    @Test
    void deleteFavoriteTest() throws Exception {
        FavoriteResponse favoriteResponse1 = new FavoriteResponse(1L, "강남역", "역삼역");
        FavoriteResponse favoriteResponse2 = new FavoriteResponse(2L, "복정역", "역삼역");
        List<FavoriteResponse> favoriteResponses = Arrays.asList(favoriteResponse1, favoriteResponse2);
        Member member = new Member(1L, DummyTestUserInfo.EMAIL, null, null);

        given(favoriteService.findAllByMemberId(any())).willReturn(new FavoritesResponse(favoriteResponses));
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(DummyTestUserInfo.EMAIL);

        String uri = "/favorites";

        mockMvc.perform(delete(uri)
                .header("Authorization", "Bearer " + "이메일 Token")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
    }
}
