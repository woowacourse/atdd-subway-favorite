package wooteco.subway.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.FavoritePathDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoritePathService;
import wooteco.subway.service.station.dto.FavoritePathResponse;

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
    void showAll() throws Exception {
        List<FavoritePathResponse> favoritePathResponses = new ArrayList<>();
        favoritePathResponses.add(new FavoritePathResponse("start station", "end station"));
        Member member = new Member();
        when(favoritePathService.findAllOf(member)).thenReturn(favoritePathResponses);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);

        MvcResult mvcResult = this.mockMvc.perform(
            get("/favoritePaths")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1OTA2NjQ2NjIsImV4cCI6MTU5MDY2ODI2Mn0.oYE1kJY5wEYGofLv83SvPDe9zQbWCryyLLqQrdIybj8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andDo(FavoritePathDocumentation.showAll())
            .andReturn();

        String expected = "[{\"startStationName\":\"start station\",\"endStationName\":\"end station\"}]";
        String responseBody = mvcResult.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo(expected);
        verify(favoritePathService).findAllOf(member);
    }

    @Test
    void registerFavoritePath() throws Exception {
        Member member = new Member();
        doNothing().when(favoritePathService).register(startStationName, endStationName, member);
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);

        this.mockMvc.perform(
            post("/favoritePaths")
                .content(inputJson)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1OTA2NjQ2NjIsImV4cCI6MTU5MDY2ODI2Mn0.oYE1kJY5wEYGofLv83SvPDe9zQbWCryyLLqQrdIybj8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoritePathDocumentation.registerFavoritePath());

        verify(favoritePathService).register(startStationName, endStationName, member);
    }

    @Test
    void deleteFavoritePath() throws Exception {
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        Member member = new Member();
        doNothing().when(favoritePathService).delete(startStationName, endStationName, member);

        this.mockMvc.perform(
            delete("/favoritePaths")
                .content(inputJson)
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicm93bkBlbWFpbC5jb20iLCJpYXQiOjE1OTA2NjQ2NjIsImV4cCI6MTU5MDY2ODI2Mn0.oYE1kJY5wEYGofLv83SvPDe9zQbWCryyLLqQrdIybj8")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent())
            .andDo(print())
            .andDo(FavoritePathDocumentation.deleteFavoritePath());

        verify(favoritePathService).delete(startStationName, endStationName, member);
    }

    // Todo: service 가 예외를 던지도록 mocking 하는 side test 해보고싶다.
}
