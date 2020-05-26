package wooteco.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.service.line.dto.*;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationCreateRequest;
import wooteco.subway.service.station.dto.StationResponse;

import java.time.LocalTime;
import java.util.List;

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
    public static final String STATION_NAME_YANGJAECITIZON = "양재시민숲역";

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

    public StationResponse createStation(String name) {
        StationCreateRequest request = new StationCreateRequest(name);

        // @formatter:off
        return
                given().
                        body(request).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post("/stations").
                then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(StationResponse.class);
        // @formatter:on
    }

    public List<StationResponse> getStations() {
        // @formatter:off
        return
                given().
                when().
                        get("/stations").
                then().
                        log().all().
                        extract().
                        jsonPath().getList(".", StationResponse.class);
        // @formatter:on
    }

    public void deleteStation(Long id) {
        // @formatter:off
        given().
        when().
                delete("/stations/" + id).
        then().
                log().all();
        // @formatter:on
    }

    public LineResponse createLine(String name) {
        LineRequest request = new LineRequest(
                name, LocalTime.of(5, 30), LocalTime.of(10, 30), 10);
        // @formatter:off
        return
                given().
                        body(request).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post("/lines").
                then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(LineResponse.class);
        // @formatter:on
    }

    public LineDetailResponse getLine(Long id) {
        // @formatter:off
        return
                given().
                when().
                        get("/lines/" + id).
                then().
                        log().all().
                        extract().as(LineDetailResponse.class);
        // @formatter:on
    }

    public void updateLine(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        LineRequest updateRequest = new LineRequest(name, startTime, endTime, intervalTime);

        // @formatter:off
        given().
                body(updateRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                put("/lines/" + id).
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    public List<LineResponse> getLines() {
        // @formatter:off
        return
                given().
                when().
                        get("/lines").
                then().
                        log().all().
                        extract().
                        jsonPath().getList(".", LineResponse.class);
        // @formatter:on
    }

    public void deleteLine(Long id) {
        // @formatter:off
        given().
        when().
                delete("/lines/" + id).
        then().
                log().all();
        // @formatter:on
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId) {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration) {
        LineStationCreateRequest request = new LineStationCreateRequest(
                preStationId, stationId, distance, duration);
        // @formatter:off
        given().
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                post("/lines/" + lineId + "/stations").
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    public void removeLineStation(Long lineId, Long stationId) {
        // @formatter:off
        given().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                delete("/lines/" + lineId + "/stations/" + stationId).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter:on
    }

    public WholeSubwayResponse retrieveWholeSubway() {
        // @formatter:off
        return
                given().
                when().
                        get("/lines/detail").
                then().
                        log().all().
                        extract().as(WholeSubwayResponse.class);
        // @formatter:on
    }

    public PathResponse findPath(String source, String target, String type) {
        // @formatter:off
        return
                given().
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get("/paths?source=" + source + "&target=" + target + "&type=" + type).
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(PathResponse.class);
        // @formatter:on
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

    public String createMember(final String email, final String name, final String password) {
        MemberRequest request = new MemberRequest(email, name, password);
        // @formatter:off
        return
                given().
                        body(request).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        when().
                        post("/members").
                        then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().header("Location");
        // @formatter:on
    }

    public TokenResponse login(final String email, final String password) {
        final LoginRequest request = new LoginRequest(email, password);

        // @formatter:off
        return
                given().
                        body(request).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post("/me/login").
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(TokenResponse.class);
        // @formatter:on
    }
}

