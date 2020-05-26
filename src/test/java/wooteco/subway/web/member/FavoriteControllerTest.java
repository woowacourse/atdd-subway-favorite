package wooteco.subway.web.member;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import wooteco.subway.service.member.favorite.FavoriteService;
import wooteco.subway.service.member.favorite.dto.FavoriteRequest;
import wooteco.subway.service.member.favorite.dto.FavoriteResponse;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FavoriteControllerTest {

    private static final Gson GSON = new Gson();
    public static final String TARGET = "선릉역";
    public static final String SOURCE = "강남역";

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private BearerAuthInterceptor bearerAuthInterceptor;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }


    @Test
    void createFavorite() throws Exception {
        FavoriteRequest request = new FavoriteRequest(SOURCE, TARGET);
        given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);

        given(favoriteService.createFavorite(any(), any())).willReturn(new FavoriteResponse(1L, SOURCE, TARGET));

        mockMvc.perform(post("/me/favorites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(GSON.toJson(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void getFavorite() throws Exception {
        given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(favoriteService.findFavorites(any())).willReturn(Arrays.asList(new FavoriteResponse(1L, SOURCE, TARGET)));

        mockMvc.perform(get("/me/favorites")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].source", Matchers.is(SOURCE)))
                .andExpect(jsonPath("$[0].target", Matchers.is(TARGET)));
    }

    @Test
    void deleteFavorite() throws Exception {
        given(bearerAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
        doNothing().when(favoriteService).deleteFavorite(any(), anyLong());

        mockMvc.perform(delete("/me/favorites/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}