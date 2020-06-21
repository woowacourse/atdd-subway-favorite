package wooteco.subway;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ActiveProfiles("test")
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

    @Value("${security.jwt.token.secret-key}")
    protected String secretKey;

    @Value("${security.jwt.token.expire-length}")
    protected Long validityInMilliseconds;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    public <T> T post(String path, Map<String, String> params, Class<T> responseType) {
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

    public void postNoReturn(String path, Map<String, String> params) {
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post(path).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    public <T> T get(String path, Class<T> responseType) {
        return
                given().when().
                        get(path).
                        then().
                        log().all().
                        extract().
                        as(responseType);
    }

    public <T> List<T> getList(String path, Class<T> responseType) {
        return
                given().when().
                        get(path).
                        then().
                        log().all().
                        extract().
                        jsonPath().getList(".", responseType);
    }

    public void updateNoReturn(String path, Map<String, String> params) {
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

    public void delete(String path) {
        given().when().
                delete(path).
                then().
                log().all();
    }

    public <T> T createByName(String path, String name, Class<T> responseType) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return post(path, params, responseType);
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

        return post("/lines", params, LineResponse.class);
    }

    public LineDetailResponse getLine(Long id) {
        return get("/lines/" + id, LineDetailResponse.class);
    }

    public void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
        Map<String, String> params = new HashMap<>();
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        updateNoReturn("/lines/" + id, params);
    }

    public List<LineResponse> getLines() {
        return getList("lines", LineResponse.class);
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

        postNoReturn("/lines/" + lineId + "/stations", params);

    }

    public void removeLineStation(Long lineId, Long stationId) {
        delete("/lines/" + lineId + "/stations/" + stationId);
    }

    public WholeSubwayResponse retrieveWholeSubway() {
        return get("/lines/detail", WholeSubwayResponse.class);
    }

    public PathResponse findPath(String source, String target, String type) {
        return get("/paths?source=" + source + "&target=" + target + "&type=" + type, PathResponse.class);
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
        StationResponse stationResponse1 = createByName("/stations", STATION_NAME_KANGNAM, StationResponse.class);
        StationResponse stationResponse2 = createByName("/stations", STATION_NAME_YEOKSAM, StationResponse.class);
        StationResponse stationResponse3 = createByName("/stations", STATION_NAME_SEOLLEUNG, StationResponse.class);
        StationResponse stationResponse4 = createByName("/stations", STATION_NAME_HANTI, StationResponse.class);
        StationResponse stationResponse5 = createByName("/stations", STATION_NAME_DOGOK, StationResponse.class);
        StationResponse stationResponse6 = createByName("/stations", STATION_NAME_MAEBONG, StationResponse.class);
        StationResponse stationResponse7 = createByName("/stations", STATION_NAME_YANGJAE, StationResponse.class);

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

    public <T> T getWithToken(String path, String token, Class<T> responseType) {
        return
                given().
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        header("Authorization", token).
                        get(path).
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(responseType);
    }

    public <T> List<T> getListWithToken(String path, String token, Class<T> responseType) {
        return
                given().
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        header("Authorization", token).
                        get(path).
                        then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().jsonPath().getList(".", responseType);
    }

    public MemberResponse getMember(String email) {
        return getWithToken("/members?email=" + email, "bearer " + createToken(), MemberResponse.class);
    }

    public String createMember(String email, String name, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        return
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

    public void postWithToken(String path, String token, Map<String, String> params) {
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                header("Authorization", token).
                put(path).
                then().
                log().all().
                statusCode(HttpStatus.OK.value());
    }

    public void deleteWithToken(String path, String token) {
        given().when().
                header("Authorization", token).
                delete(path).
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    public void updateMember() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "NEW_" + TEST_USER_NAME);
        params.put("password", "NEW_" + TEST_USER_PASSWORD);

        postWithToken("/members", "bearer " + createToken(), params);
    }

    public void createFavorite(String sourceName, String destinationName) {
        Map<String, String> params = new HashMap<>();
        params.put("sourceName", sourceName);
        params.put("destinationName", destinationName);

        postWithToken("/favorites", "bearer " + createToken(), params);
    }

    public List<FavoriteResponse> getFavorites() {
        return getListWithToken("/favorites", "bearer " + createToken(), FavoriteResponse.class);
    }

    public void deleteMember() {
        deleteWithToken("/members", "bearer " + createToken());
    }

    public void deleteFavorite(String sourceName, String destinationName) {
        Map<String, String> params = new HashMap<>();
        params.put("sourceName", sourceName);
        params.put("destinationName", destinationName);
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                header("Authorization", "bearer " + createToken()).
                delete("/favorites").
                then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
    }

    private String createToken() {
        return new JwtTokenProvider(secretKey, validityInMilliseconds).createToken(TEST_USER_EMAIL);
    }
}

