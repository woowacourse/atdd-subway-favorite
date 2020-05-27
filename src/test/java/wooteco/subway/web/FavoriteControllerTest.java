package wooteco.subway.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoritePathService;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Sql("/truncate.sql")
class FavoriteControllerTest {

    @MockBean
    private FavoritePathService favoritePathService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private MockMvc mockMvc;

    private final String startStationName = "신정역";
    private final String endStationName = "목동역";
    private final String inputJson = "{\"startStationName\":\"" + startStationName + "\"," +
        "\"endStationName\":\"" + endStationName + "\"}";

    @BeforeEach
    private void setUp(
        WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    void registerFavoritePath() throws Exception {
        Member member = new Member();
        doNothing().when(favoritePathService).register(startStationName, endStationName, member);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);

        this.mockMvc.perform(
            post("/favorites")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andDo(print());

        verify(favoritePathService).register(startStationName, endStationName, member);
    }

    @Test
    void deleteFavoritePath() throws Exception {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        Member member = new Member();
        doNothing().when(favoritePathService).delete(startStationName, endStationName, member);

        this.mockMvc.perform(
            delete("/favorites")
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent())
            .andDo(print());

        verify(favoritePathService).delete(startStationName, endStationName, member);
    }

    // Todo: service 가 예외를 던지도록 mocking 하는 side test 해보고싶다.
}
