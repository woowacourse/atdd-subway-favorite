package wooteco.subway.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
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
public class FavoriteControllerTest {

    @MockBean
    protected FavoritePathService favoritePathService;
    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(
        WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
    }

    @Test
    public void registerFavoritePath() throws Exception {
        String startStationName = "신정역";
        String endStationName = "목동역";
        String inputJson = "{\"startStationName\":\"" + startStationName + "\"," +
            "\"endStationName\":\"" + endStationName + "\"}";

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
}
