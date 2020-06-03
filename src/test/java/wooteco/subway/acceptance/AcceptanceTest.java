package wooteco.subway.acceptance;

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
import wooteco.subway.service.member.dto.LoginRequest;
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

    /**
     * 강남 - 역삼 - 선릉
     * |           |
     * |          한티
     * |           |
     * 양재 - 매봉 - 도곡
     */
    public void initStation() {
        // 역 등록
        createStation(STATION_NAME_KANGNAM);
        createStation(STATION_NAME_YEOKSAM);
        createStation(STATION_NAME_SEOLLEUNG);
        createStation(STATION_NAME_HANTI);
        createStation(STATION_NAME_DOGOK);
        createStation(STATION_NAME_MAEBONG);
        createStation(STATION_NAME_YANGJAE);

        // 2호선
        createLine(LINE_NAME_2);
        List<LineResponse> lines = getLines();
        List<StationResponse> stations = getStations();
        addLineStation(lines.get(0).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(0).getId(), stations.get(0).getId(), stations.get(1).getId());
        addLineStation(lines.get(0).getId(), stations.get(1).getId(), stations.get(2).getId());

        // 분당선
        createLine(LINE_NAME_BUNDANG);
        lines = getLines();
        addLineStation(lines.get(1).getId(), null, stations.get(2).getId());
        addLineStation(lines.get(1).getId(), stations.get(2).getId(), stations.get(3).getId());
        addLineStation(lines.get(1).getId(), stations.get(3).getId(), stations.get(4).getId());

        // 3호선
        createLine(LINE_NAME_3);
        lines = getLines();
        addLineStation(lines.get(2).getId(), null, stations.get(4).getId());
        addLineStation(lines.get(2).getId(), stations.get(4).getId(), stations.get(5).getId());
        addLineStation(lines.get(2).getId(), stations.get(5).getId(), stations.get(6).getId());

        // 신분당선
        createLine(LINE_NAME_SINBUNDANG);
        lines = getLines();
        addLineStation(lines.get(3).getId(), null, stations.get(0).getId());
        addLineStation(lines.get(3).getId(), stations.get(0).getId(), stations.get(6).getId());
    }

    public void createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        post("/stations", params);
    }

    public List<StationResponse> getStations() {
        return getAll("/stations", StationResponse.class);
    }

    public void deleteStation(Long id) {
        delete("/stations/" + id);
    }

    public void createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", String.valueOf(10));

        post("/lines", params);
    }

    public List<LineResponse> getLines() {
        return getAll("/lines", LineResponse.class);
    }

    public LineDetailResponse getLine(Long id) {
        return get("/lines/" + id, LineDetailResponse.class);
    }

    public void updateLine(Long id, String name, LocalTime startTime, LocalTime endTime, int intervalTime) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", String.valueOf(intervalTime));

        put("/lines/" + id, params);
    }

    public void deleteLine(Long id) {
        delete("/lines/" + id);
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId) {
        Map<String, String> params = new HashMap<>();
        params.put("lineId", String.valueOf(lineId));
        params.put("preStationId", String.valueOf(preStationId));
        params.put("stationId", String.valueOf(stationId));
        params.put("distance", String.valueOf(10));
        params.put("duration", String.valueOf(10));

        post("/lines/" + lineId + "/stations", params);
    }

    public void removeLineStation(Long lineId, Long stationId) {
        delete("/lines/" + lineId + "/stations/" + stationId);
    }

    public WholeSubwayResponse getWholeSubway() {
        return get("/lines/detail", WholeSubwayResponse.class);
    }

    public void createMember(final String email, final String name, final String password) {
        final Map<String, String> param = new HashMap<>();
        param.put("email", email);
        param.put("name", name);
        param.put("password", password);

        post("/members", param);
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

    public <T> void post(String path, Map<String, String> params) {
        // @formatter:off
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                post(path).
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        // @formatter:on
    }

    public <T> T get(String path, Class<T> responseType) {
        // @formatter:off
        return
                given().
                when().
                        get(path).
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(responseType);
        // @formatter:on
    }

    public <T> List<T> getAll(String path, Class<T> responseType) {
        // @formatter:off
        return
                given().
                when().
                        get(path).
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().
                        jsonPath().
                        getList(".",responseType);
        // @formatter:on
    }

    public <T> void put(String path, Map<String, String> params) {
        // @formatter:off
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                put(path).
        then().
                log().all().
                statusCode(HttpStatus.OK.value());
        // @formatter:on
    }

    public <T> void delete(String path) {
        // @formatter:off
        given().
        when().
                delete(path).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter:on
    }
}

