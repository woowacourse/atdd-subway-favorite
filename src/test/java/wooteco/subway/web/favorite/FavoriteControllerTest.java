package wooteco.subway.web.favorite;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.web.member.MemberControllerTest.*;

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
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import wooteco.subway.doc.FavoriteDocumentation;
import wooteco.subway.domain.favorite.FavoriteStation;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.FavoritesResponse;
import wooteco.subway.service.member.dto.TokenResponse;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql("/truncate.sql")
class FavoriteControllerTest {
    @LocalServerPort
    public int port;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @Autowired
    private ObjectMapper objectMapper;

    private FavoriteStation favoriteStation;
    private TokenResponse response;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
        RestDocumentationContextProvider restDocumentation) throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .addFilter(new ShallowEtagHeaderFilter())
            .apply(documentationConfiguration(restDocumentation))
            .build();
        RestAssured.port = port;

        favoriteStation = new FavoriteStation(1L, "gangnam", "jamsil");
        createMember();
        response = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @Test
    void create() throws Exception {
        doNothing().when(favoriteService).save(any(), any());

        mockMvc.perform(post("/favorites")
            .header("Authorization", "Bearer " + response.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(favoriteStation))
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.createFavorite());
    }

    @Test
    void showAll() throws Exception {
        when(favoriteService.findAll(any())).thenReturn(
            new FavoritesResponse(Arrays.asList(new FavoriteStation(1L, "gangnam", "jamsil"))));

        mockMvc.perform(get("/favorites")
            .header("Authorization", "Bearer " + response.getAccessToken())
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.findFavorites());
    }

    @Test
    void deleteByNames() throws Exception {
        doNothing().when(favoriteService).delete(any(), any(), any());

        mockMvc.perform(delete("/favorites")
            .param("source", "gangnam")
            .param("target", "jamsil")
            .header("Authorization", "Bearer " + response.getAccessToken())
        )
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(FavoriteDocumentation.deleteFavorite());
    }

    private TokenResponse login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return
            given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/oauth/token").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(TokenResponse.class);
    }

    private void createMember() throws Exception {
        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andDo(print());
    }
}