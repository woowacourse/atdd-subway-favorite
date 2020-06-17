package wooteco.subway.acceptance;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.util.HttpMethod;

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

	public static RequestSpecification given() {
		return RestAssured.given().log().all();
	}

	@BeforeEach
	public void setUp() {
		RestAssured.port = port;
	}

	public StationResponse createStation(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);

		return HttpMethod.POST.requestWithBody("/stations", StationResponse.class, params);
	}

	public List<StationResponse> getStations() {
		return
			given().when().
				get("/stations").
				then().
				log().all().
				extract().
				jsonPath().getList(".", StationResponse.class);
	}

	public void deleteStation(Long id) {
		HttpMethod.DELETE.request("/stations/" + id);
	}

	public LineResponse createLine(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		return HttpMethod.POST.requestWithBody("/lines", LineResponse.class, params);
	}

	public LineDetailResponse getLine(Long id) {
		return HttpMethod.GET.requestWithBody("/lines/" + id, LineDetailResponse.class);
	}

	public void updateLine(Long id, LocalTime startTime, LocalTime endTime) {
		Map<String, String> params = new HashMap<>();
		params.put("startTime", startTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("endTime", endTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
		params.put("intervalTime", "10");

		HttpMethod.PUT.request("/lines/" + id, params);
	}

	public List<LineResponse> getLines() {
		return
			given().when().
				get("/lines").
				then().
				log().all().
				extract().
				jsonPath().getList(".", LineResponse.class);
	}

	public void deleteLine(Long id) {
		HttpMethod.DELETE.request("/lines/" + id);
	}

	public void addLineStation(Long lineId, Long preStationId, Long stationId) {
		addLineStation(lineId, preStationId, stationId, 10, 10);
	}

	public void addLineStation(Long lineId, Long preStationId, Long stationId, Integer distance,
		Integer duration) {
		Map<String, String> params = new HashMap<>();
		params.put("preStationId", preStationId == null ? "" : preStationId.toString());
		params.put("stationId", stationId.toString());
		params.put("distance", distance.toString());
		params.put("duration", duration.toString());

		HttpMethod.POST.request("/lines/" + lineId + "/stations", params);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		HttpMethod.DELETE.request("/lines/" + lineId + "/stations/" + stationId);
	}

	public WholeSubwayResponse retrieveWholeSubway() {
		return HttpMethod.GET.requestWithBody("/lines/detail", WholeSubwayResponse.class);
	}

	public PathResponse findPath(String source, String target, String type) {
		String url = "/paths?source=" + source + "&target=" + target + "&type=" + type;

		return HttpMethod.GET
			.requestWithBody(url, PathResponse.class);
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
		addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId(), 5,
			10);
		addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId(), 5,
			10);

		// 분당선
		LineResponse lineResponse2 = createLine("분당선");
		addLineStation(lineResponse2.getId(), null, stationResponse3.getId(), 0, 0);
		addLineStation(lineResponse2.getId(), stationResponse3.getId(), stationResponse4.getId(), 5,
			10);
		addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId(), 5,
			10);

		// 3호선
		LineResponse lineResponse3 = createLine("3호선");
		addLineStation(lineResponse3.getId(), null, stationResponse5.getId(), 0, 0);
		addLineStation(lineResponse3.getId(), stationResponse5.getId(), stationResponse6.getId(), 5,
			10);
		addLineStation(lineResponse3.getId(), stationResponse6.getId(), stationResponse7.getId(), 5,
			10);

		// 신분당선
		LineResponse lineResponse4 = createLine("신분당선");
		addLineStation(lineResponse4.getId(), null, stationResponse1.getId(), 0, 0);
		addLineStation(lineResponse4.getId(), stationResponse1.getId(), stationResponse7.getId(),
			40, 3);
	}

	public void createMember(String email, String name, String password) {
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		params.put("name", name);
		params.put("password", password);

		HttpMethod.POST.request("/me/sign_up", params);
	}

	public MemberResponse getMember(TokenResponse tokenResponse) {
		return
			getDefaultGiven(tokenResponse.getAccessToken())
				.when()
				.get("/me")
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value())
				.extract().as(MemberResponse.class);
	}

	public void deleteMember(TokenResponse tokenResponse) {
		getDefaultGiven(tokenResponse.getAccessToken())
			.when()
			.delete("/me")
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value());
	}

	public TokenResponse login(String email, String password) {
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("email", email);
		paramMap.put("password", password);

		return getDefaultGiven()
			.body(paramMap)
			.when()
			.post("/me/login")
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value())
			.extract().as(TokenResponse.class);
	}

	public void updateMemberWhenLoggedIn(String accessToken,
		String name,
		String password) {
		Map<String, String> params = new HashMap<>();
		params.put("name", name);
		params.put("password", password);

		getDefaultGiven(accessToken)
			.body(params)
			.when()
			.put("/me")
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value());
	}

	public MemberResponse myInfoWithBearerAuth(TokenResponse tokenResponse) {
		return
			getDefaultGiven(tokenResponse.getAccessToken()).
				when().
				get("/me").
				then().
				log().all().
				extract().as(MemberResponse.class);
	}

	protected RequestSpecification getDefaultGiven(String accessToken) {
		return given()
			.header("Authorization", "bearer " + accessToken)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.accept(MediaType.APPLICATION_JSON_VALUE);
	}

	protected RequestSpecification getDefaultGiven() {
		return getDefaultGiven(Strings.EMPTY);
	}
}

