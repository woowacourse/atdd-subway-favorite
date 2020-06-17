package wooteco.subway.web.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.web.member.LoginMemberMethodArgumentResolver;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {

    private static final String token = "bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTkwMzk2NTM4LCJleHAiOjE1OTA0MDAxMzh9.hGKWE4UOqfoLQ5-MdhovWGhqNkOXuJFJTEDYRZHHsyk";

    @MockBean
    protected FavoriteService favoriteService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();

        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(loginMemberMethodArgumentResolver.supportsParameter(any())).willReturn(true);
        given(loginMemberMethodArgumentResolver.resolveArgument(any(), any(), any(),
            any())).willReturn(1L);
    }

    @Test
    void createFavorite() throws Exception {
        String favoriteRequest = "{\"source\":1,\"target\":2}";
        given(favoriteService.addFavorite(anyLong(), any(FavoriteRequest.class))).willReturn(1L);

        this.mockMvc.perform(post("/me/favorites")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", token)
            .content(favoriteRequest))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(FavoriteDocumentation.addFavorite());
    }

    @Test
    void getFavorite() throws Exception {
        Long id = 3L;
        StationResponse source = new StationResponse(1L, "강남역", LocalDateTime.now());
        StationResponse target = new StationResponse(2L, "역삼역", LocalDateTime.now());
        FavoriteResponse favoriteResponse = new FavoriteResponse(id, source, target);
        given(favoriteService.getFavorite(anyLong(), anyLong(), anyLong())).willReturn(
            favoriteResponse);

        String response = this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/me/favorites/source/{source}/target/{target}", 1,
                2)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.getFavorite())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(favoriteResponse));
    }

    @Test
    void readFavorites() throws Exception {
        Long id = 3L;
        StationResponse source = new StationResponse(1L, "강남역", LocalDateTime.now());
        StationResponse target = new StationResponse(2L, "역삼역", LocalDateTime.now());
        List<FavoriteResponse> favoriteResponses = Arrays.asList(
            new FavoriteResponse(id, source, target), new FavoriteResponse(id, source, target));
        given(favoriteService.getFavorites(any())).willReturn(favoriteResponses);

        String response = this.mockMvc.perform(
            RestDocumentationRequestBuilders.get("/me/favorites")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.getFavorites())
            .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertThat(response).isEqualTo(objectMapper.writeValueAsString(favoriteResponses));
    }

    @Test
    void deleteFavorite() throws Exception {
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/me/favorites/source/{source}/target/{target}",
                1, 2)
                .header("Authorization", token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(FavoriteDocumentation.removeFavorite());

        verify(favoriteService).removeFavorite(anyLong(), anyLong(), anyLong());
    }

    @Test
    void deleteFavoriteById() throws Exception {
        this.mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/me/favorites/{favoriteId}", 1)
                .header("Authorization", token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(FavoriteDocumentation.removeFavoriteById());

        verify(favoriteService).removeFavoriteById(anyLong(), anyLong());
    }
}
