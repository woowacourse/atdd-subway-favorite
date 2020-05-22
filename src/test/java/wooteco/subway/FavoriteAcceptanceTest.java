package wooteco.subway;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.engine.TestExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.path.dto.PathResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class FavoriteAcceptanceTest extends AcceptanceTest {
    TokenResponse tokenResponse;
    PathResponse PathResponse;

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
                createLine("1호선");
                createStation("강남역");
                createStation("분당역");
                createStation("잠실역");
                addLineStation(1L, null, 1L);
                addLineStation(1L, 1L, 2L);
                addLineStation(1L, 2L, 3L);
            }),
            DynamicTest.dynamicTest("경로 조회 요청을 한다.", () -> {
                PathResponse = findPath("강남역", "잠실역", "DISTANCE");
            }),
            DynamicTest.dynamicTest("사용자가 즐겨찾기를 추가한다.", () -> {
                addFavorite(tokenResponse, 1L, 3L);
            }),
            DynamicTest.dynamicTest("즐겨찾기 페이지에 자신이 저장한 즐겨찾기가 있다.", () -> {
                // assertThat(getFavorites(tokenResponse)).hasSize(1);
            }),
            DynamicTest.dynamicTest("즐겨찾기 페이지에서 재조회를 요청한다", () -> {
                // findPath(tokenResponse, 1L);
            }),
            DynamicTest.dynamicTest("즐겨찾기 목록에서 삭제할 수 있다.", () -> {
                // deleteFavorite(tokenResponse, 1L);
            })
        );
    }

    private void addFavorite(TokenResponse tokenResponse, long source, long target) throws Exception {
        Map<String, Long> model = new HashMap<>();
        model.put("source", source);
        model.put("target", target);
        this.mockMvc.perform(post("/favorites")
            .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
            .content(objectMapper.writeValueAsString(model))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andDo(print());

    }

    public TokenResponse login(String email, String password) {
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

}
