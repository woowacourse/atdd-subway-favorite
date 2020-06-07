package wooteco.subway.acceptance.favorite;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wooteco.subway.acceptance.AuthAcceptanceTest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.favorite.dto.FavoritesResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FavoriteAcceptanceTest extends AuthAcceptanceTest {
    TokenResponse tokenResponse;
    PathResponse PathResponse;

    private StationResponse stationResponse1;
    private StationResponse stationResponse2;
    private StationResponse stationResponse3;
    private LineResponse lineResponse;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @TestFactory
    Stream<DynamicTest> mangeFavorite() {
        return Stream.of((
                        DynamicTest.dynamicTest("회원가입을 한다.", () -> {
                            createMember(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
                        })),
                DynamicTest.dynamicTest("로그인이 되어있다. ", () -> {
                    tokenResponse = login(TEST_USER_EMAIL, TEST_USER_PASSWORD);
                }),
                DynamicTest.dynamicTest("지하철과 노선이 등록되어 있다.", () -> {
                    lineResponse = createLine("1호선");
                    stationResponse1 = createStation("gangnam");
                    stationResponse2 = createStation("bundang");
                    stationResponse3 = createStation("jamsil");
                    addLineStation(lineResponse.getId(), null, stationResponse1.getId());
                    addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
                    addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());
                }),
                DynamicTest.dynamicTest("경로 조회 요청을 한다.", () -> {
                    PathResponse = findPath("gangnam", "jamsil", "DISTANCE");
                }),
                DynamicTest.dynamicTest("사용자가 즐겨찾기를 추가한다.", () -> {
                    addFavorite(tokenResponse, "gangnam", "jamsil");
                }),
                DynamicTest.dynamicTest("즐겨찾기 페이지에 자신이 저장한 즐겨찾기가 있다.", () -> {
                    assertThat(getFavorites(tokenResponse).getFavoriteStations()).hasSize(1);
                }),
                DynamicTest.dynamicTest("즐겨찾기 페이지에서 재조회를 요청한다", () -> {
                    List<FavoriteResponse> favoriteStations = getFavorites(tokenResponse).getFavoriteStations();
                    findPath(favoriteStations.get(0).getSource().getName(),
                            favoriteStations.get(0).getTarget().getName(), "DISTANCE");
                }),
                DynamicTest.dynamicTest("즐겨찾기 목록에서 삭제할 수 있다.", () -> {
                    deleteFavorite(tokenResponse, stationResponse1.getId(), stationResponse3.getId());
                    assertThat(getFavorites(tokenResponse).getFavoriteStations()).hasSize(0);
                })
        );
    }

    private void addFavorite(TokenResponse tokenResponse, String source, String target) throws Exception {
        Map<String, String> model = new HashMap<>();
        model.put("source", source);
        model.put("target", target);
        this.mockMvc.perform(post("/favorites")
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .content(objectMapper.writeValueAsString(model))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    private FavoritesResponse getFavorites(TokenResponse tokenResponse) throws Exception {
        final MvcResult authorization = this.mockMvc.perform(get("/favorites")
                .header("Authorization", "Bearer" + tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        final String result = authorization.getResponse().getContentAsString();
        return objectMapper.readValue(result, FavoritesResponse.class);
    }

    private void deleteFavorite(TokenResponse tokenResponse, Long source, Long target) throws Exception {
        mockMvc.perform(delete("/favorites")
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .param("source", String.valueOf(source))
                .param("target", String.valueOf(target))
        )
                .andExpect(status().isOk());
    }

}
