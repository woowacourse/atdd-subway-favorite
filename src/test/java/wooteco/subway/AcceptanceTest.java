package wooteco.subway;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.member.dto.MemberFavoriteResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.path.dto.PathResponse;
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

    public static final String TEST_INVALID_USER_EMAIL = "brown.borwn";
    public static final String TEST_INVALID_USER_NAME = "";

    public static final Long TEST_START_STATION_ID = 1L;
    public static final Long TEST_END_STATION_ID = 2L;

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    public StationResponse createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return post(params, "/stations", StationResponse.class);
    }

    public List<StationResponse> getStations() {
        return getList("/stations", StationResponse.class);
    }

    public void deleteStation(Long id) {
        delete("/stations/" + id);
    }

    public LineResponse createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        return post(params, "/lines", LineResponse.class);
    }

    public LineDetailResponse getLine(Long id) {
        return get("/lines/" + id, LineDetailResponse.class);
    }

    public void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        update(params,"/lines/" + id);
    }

    public List<LineResponse> getLines() {
        return getList("/lines", LineResponse.class);
    }

    public void deleteLine(Long id) {
        delete("/lines/" + id);
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

        postNoReturn(params, "/lines/" + lineId + "/stations");
    }

    public void removeLineStation(Long lineId, Long stationId) {
        delete("/lines/" + lineId + "/stations/" + stationId);
    }

    public WholeSubwayResponse retrieveWholeSubway() {
        return get("/lines/detail", WholeSubwayResponse.class);
    }

    public PathResponse findPath(String source, String target, String type) {
        String url = "/paths?source=" + source + "&target=" + target + "&type=" + type;
        return get(url, PathResponse.class);
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

    public void createMember(String email, String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/members").
                then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().header("Location");
    }

    public MemberResponse getMember(String email) {
        return get("/members?email=" + email, MemberResponse.class);
    }

    public void updateMember(TokenResponse tokenResponse) {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        updateByToken(tokenResponse, params, "/members");
    }

    public void deleteMember(TokenResponse tokenResponse) {
        deleteByToken(tokenResponse, "/members");
    }

    public void createFavorite(TokenResponse tokenResponse, Long startStationId, Long endStationId) {
        Map<String, String> params = new HashMap<>();
        params.put("startStationId", startStationId.toString());
        params.put("endStationId", endStationId.toString());

        postByToken(tokenResponse, params, "/members/favorite");
    }

    public MemberFavoriteResponse findFavoriteById(TokenResponse tokenResponse) {
        return getByToken(tokenResponse, "/members/favorite", MemberFavoriteResponse.class);
    }

    public void deleteFavoriteById(TokenResponse tokenResponse, Long id) {
        deleteByToken(tokenResponse, "/members/favorite/" + id);
    }

    protected <T> T get(String url, Class<T> responseType) {
        return given().when().
                get(url).
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(responseType);
    }

    protected <T> List<T> getList(String url, Class<T> responseType) {
        return given().when().
                get(url).
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().
                jsonPath().getList(".", responseType);
    }

    protected <T> T post(Map<String, String> params, String url, Class<T> responseType) {
        return given().
                    body(params).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                    post(url).
                then().
                    log().all().
                    statusCode(HttpStatus.CREATED.value()).
                    extract().as(responseType);
    }

    protected void postNoReturn(Map<String, String> params, String url) {
        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post(url).
        then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    protected void update(Map<String, String> params, String url) {
        given().
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            put(url).
        then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    protected void delete(String url) {
        given().when().
            delete(url).
        then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }

    protected <T> T getByToken(TokenResponse tokenResponse, String url, Class<T> responseType) {
        return given().auth().
                    oauth2(tokenResponse.getAccessToken()).
                    accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                    get(url).
                then().
                    log().all().
                    statusCode(HttpStatus.OK.value()).
                    extract().as(responseType);
    }

    protected void postByToken(TokenResponse tokenResponse, Map<String, String> params, String url) {
        given().auth().
            oauth2(tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            post(url).
        then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    protected void updateByToken(TokenResponse tokenResponse, Map<String, String> params, String url) {
        given().auth().
            oauth2(tokenResponse.getAccessToken()).
            body(params).
            contentType(MediaType.APPLICATION_JSON_VALUE).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            put(url).
        then().
            log().all().
            statusCode(HttpStatus.OK.value());
    }

    protected void deleteByToken(TokenResponse tokenResponse, String url) {
        given().auth().
            oauth2(tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON_VALUE).
        when().
            delete(url).
        then().
            log().all().
            statusCode(HttpStatus.NO_CONTENT.value());
    }
}