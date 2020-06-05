package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {
    public static final String STATION_NAME_KANGNAM = "강남역";
    public static final String STATION_NAME_YEOKSAM = "역삼역";
    public static final String STATION_NAME_SEOLLEUNG = "선릉역";
    public static final String STATION_NAME_HANTI = "한티역";
    public static final String STATION_NAME_DOGOK = "도곡역";
    public static final String STATION_NAME_MAEBONG = "매봉역";
    public static final String STATION_NAME_YANGJAE = "양재역";

    public static final String LINE_NAME_2 = "2호선";
    public static final String LINE_NAME_3 = "3호선";
    public static final String LINE_NAME_BUNDANG = "분당선";
    public static final String LINE_NAME_SINBUNDANG = "신분당선";

    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    private <T> T post(String path, Map<String, String> params, Class<T> responseType) {
        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post(path).
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(responseType);
    }

    private String postWithLocation(String path, Map<String, String> params) {
        return
                given().
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post(path).
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().header("Location");
    }

    protected <T> T get(String path, Class<T> responseType) {
        return
                given().
                        when().
                        get(path).
                        then().
                        log().all().
                        extract().as(responseType);
    }

    protected <T> List<T> getListOf(String path, Class<T> responseType) {
        return
                given().
                        when().
                        get(path).
                        then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", responseType);
    }

    protected <T> T getWithAuth(String path, Class<T> responseType, String sessionId, String token) {
        return
                given().
                        cookie("JSESSIONID", sessionId).
                        header("Authorization", token).
                        when().
                        get(path).
                        then().
                        log().all().
                        extract().as(responseType);
    }

    protected void put(Map<String, String> params, String path) {
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                put(path).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    protected void delete(String path) {
        given().
                when().
                delete(path).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    public StationResponse createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return post("stations", params, StationResponse.class);
    }


    public LineResponse createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        return post("/lines", params, LineResponse.class);
    }

    public LineDetailResponse getLine(Long id) {
        return get("lines/" + id, LineDetailResponse.class);
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId == null ? "" : preStationId.toString());
        params.put("stationId", stationId.toString());
        params.put("distance", distance.toString());
        params.put("duration", duration.toString());

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/lines/" + lineId + "/stations").
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }


    /**
     * 강남 - 역삼 - 선릉
     * |           |
     * |          한티
     * |           |
     * 양재 - 매봉 - 도곡
     */
    public void initStation() {
        // 역 등록
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
        StationResponse stationResponse4 = createStation(STATION_NAME_HANTI);
        StationResponse stationResponse5 = createStation(STATION_NAME_DOGOK);
        StationResponse stationResponse6 = createStation(STATION_NAME_MAEBONG);
        StationResponse stationResponse7 = createStation(STATION_NAME_YANGJAE);

        // 2호선
        LineResponse lineResponse1 = createLine("2호선");
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId(), 0, 0);
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 5, 10);
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId(), 5, 10);

        // 분당선
        LineResponse lineResponse2 = createLine("분당선");
        addLineStation(lineResponse2.getId(), null, stationResponse3.getId(), 0, 0);
        addLineStation(lineResponse2.getId(), stationResponse3.getId(), stationResponse4.getId(), 5, 10);
        addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId(), 5, 10);

        // 3호선
        LineResponse lineResponse3 = createLine("3호선");
        addLineStation(lineResponse3.getId(), null, stationResponse5.getId(), 0, 0);
        addLineStation(lineResponse3.getId(), stationResponse5.getId(), stationResponse6.getId(), 5, 10);
        addLineStation(lineResponse3.getId(), stationResponse6.getId(), stationResponse7.getId(), 5, 10);

        // 신분당선
        LineResponse lineResponse4 = createLine("신분당선");
        addLineStation(lineResponse4.getId(), null, stationResponse1.getId(), 0, 0);
        addLineStation(lineResponse4.getId(), stationResponse1.getId(), stationResponse7.getId(), 40, 3);
    }

    public String createMember(String email, String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        return postWithLocation("/members", params);
    }

    public MemberResponse getMember(String email, String sessionId, TokenResponse tokenResponse) {
        return getWithAuth("/members?email=" + email, MemberResponse.class, sessionId,
                tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());
    }

    public Response login(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members/login");
    }

    public <T> T getResponse(Class<T> responseType, Response response) {
        return response.
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                and().
                extract().as(responseType);
    }
}

