package woowa.bossdog.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowa.bossdog.subway.service.member.dto.LoginRequest;
import woowa.bossdog.subway.service.member.dto.MemberResponse;
import woowa.bossdog.subway.service.member.dto.TokenResponse;
import woowa.bossdog.subway.service.member.dto.UpdateMemberRequest;
import woowa.bossdog.subway.service.line.dto.LineDetailResponse;
import woowa.bossdog.subway.service.line.dto.LineResponse;
import woowa.bossdog.subway.service.line.dto.UpdateLineRequest;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AcceptanceTest {

    @LocalServerPort
    public int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    void createLine(final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", String.valueOf(intervalTime));
        post("lines", params);
    }

    List<LineResponse> listLines() {
        return getAll("/lines", LineResponse.class);
    }

    void updateLine(final Long id, final UpdateLineRequest updateRequest) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", updateRequest.getName());
        params.put("startTime", updateRequest.getStartTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", updateRequest.getEndTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", String.valueOf(updateRequest.getIntervalTime()));

        put("/lines/" + id, params);
    }

    LineResponse findLine(final Long id) {
        return getOne("/lines/" + id, LineResponse.class);
    }

    void removeLine(final Long id) {
        delete("/lines/" + id);
    }

    void createStation(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        post("/stations", params);
    }

    List<StationResponse> listStations() {
        return getAll("/stations", StationResponse.class);
    }

    StationResponse findStation(final Long id) {
        return getOne("/stations/" + id, StationResponse.class);
    }

    void removeStation(final Long id) {
        delete("/stations/" + id);
    }

    void addLineStation(final Long lineId, final Long preStationId, final Long stationId, final int distance, final int duration) {
        final Map<String, String> params = new HashMap<>();
        params.put("preStationId", Objects.isNull(preStationId) ? null : String.valueOf(preStationId));
        params.put("stationId", String.valueOf(stationId));
        params.put("distance", String.valueOf(distance));
        params.put("duration", String.valueOf(duration));

        post("/lines/" + lineId + "/stations", params);
    }

    void removeLineStation(final Long lineId, final Long stationId) {
        delete("/lines/" + lineId + "/stations/" + stationId);
    }

    LineDetailResponse findLineWithStations(final Long lineId) {
        return getOne("/lines/" + lineId + "/stations", LineDetailResponse.class);
    }


    void createMember(final String email, final String name, final String password) {
        final Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("name", name);
        params.put("password", password);

        post("/members", params);
    }

    List<MemberResponse> listMembers() {
        return getAll("/members", MemberResponse.class);
    }

    MemberResponse findMember(final Long id) {
        return getOne("/members/" + id, MemberResponse.class);
    }

    void updateMember(final Long id, UpdateMemberRequest request) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", request.getName());
        params.put("password", request.getPassword());
        put("/members/" + id, params);
    }

    void deleteMember(final Long id) {
        delete("/members/" + id);
    }

    TokenResponse loginMember(final String email, final String password) {
        final LoginRequest request = new LoginRequest(email, password);

        // @formatter : off
        return given().
                        accept(MediaType.APPLICATION_JSON_VALUE).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        body(request).
                when().
                        post("/me/login").
                then().
                        log().all().
                        statusCode(HttpStatus.OK.value()).
                        extract().as(TokenResponse.class);
        // @formatter : on
    }

    protected <T> void post(String path, Map<String, String> params) {
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

    protected <T> T getOne(String path, Class<T> responseType) {
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

    protected <T> List<T> getAll(String path, Class<T> responseType) {
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

    protected <T> void put(String path, Map<String, String> params) {
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

    protected <T> void delete(String path) {
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
