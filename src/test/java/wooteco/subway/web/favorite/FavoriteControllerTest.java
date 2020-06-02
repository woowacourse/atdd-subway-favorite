package wooteco.subway.web.favorite;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberResponse;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
class FavoriteControllerTest {
    private final String uri = "/favorites";

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    void createFavorite() throws Exception {
        given(favoriteService.create(any(), any())).willReturn(1L);

        String inputJson = "{\"departureId\" : \"1\", \"arrivalId\" : \"3\"}";

        String email = "test@test.com";
        given(memberService.findMemberByEmail(email)).willReturn(new MemberResponse(1L, email, "test"));
        String token = jwtTokenProvider.createToken(email);

        mockMvc.perform(post(uri)
            .header("Authorization", "bearer " + token)
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andDo(FavoriteDocumentation.createFavorite());
    }

    @Test
    void showFavorites() throws Exception {
        Station jamsil = new Station(1L, "잠실");
        Station gangnam = new Station(2L, "강남");
        FavoriteResponse favoriteResponse = new FavoriteResponse(jamsil.getId(), gangnam.getId());
        List<FavoriteResponse> favoriteResponses = Collections.singletonList(favoriteResponse);
        given(favoriteService.findAll(any())).willReturn(favoriteResponses);

        String email = "test@test.com";
        given(memberService.findMemberByEmail(email)).willReturn(new MemberResponse(1L, email, "test"));
        String token = jwtTokenProvider.createToken(email);

        MvcResult mvcResult = mockMvc.perform(get(uri)
            .header("Authorization", "bearer " + token))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(FavoriteDocumentation.showFavorites())
            .andReturn();

        assertThat(mvcResult.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    void deleteFavorite() throws Exception {
        String inputJson = "{\"departureId\" : \"1\", \"arrivalId\" : \"3\"}";

        mockMvc.perform(delete(uri)
            .header("Authorization", "bearer tokenValues")
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andDo(FavoriteDocumentation.deleteFavorite());
    }
}