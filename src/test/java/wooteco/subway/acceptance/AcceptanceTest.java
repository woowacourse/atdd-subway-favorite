package wooteco.subway.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationCreateRequest;
import wooteco.subway.service.station.dto.StationResponse;
import wooteco.subway.web.exceptions.ErrorResponse;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class AcceptanceTest {

    protected static final String STATION_NAME_KANGNAM = "강남역";
    protected static final String STATION_NAME_YEOKSAM = "역삼역";
    protected static final String STATION_NAME_SEOLLEUNG = "선릉역";
    protected static final String STATION_NAME_HANTI = "한티역";
    protected static final String STATION_NAME_DOGOK = "도곡역";
    protected static final String STATION_NAME_MAEBONG = "매봉역";
    protected static final String STATION_NAME_YANGJAE = "양재역";
    protected static final String LINE_NAME_2 = "2호선";
    protected static final String LINE_NAME_3 = "3호선";
    protected static final String LINE_NAME_BUNDANG = "분당선";
    protected static final String LINE_NAME_SINBUNDANG = "신분당선";
    protected static final String TEST_USER_EMAIL = "brown@email.com";
    protected static final String TEST_USER_NAME = "브라운";
    protected static final String TEST_USER_PASSWORD = "brown";
    protected static final String TEST_USER_EMAIL2 = "turtle@gmail.com";
    protected static final String TEST_USER_NAME2 = "turtle";
    protected static final String TEST_USER_PASSWORD2 = "1111";
    protected static final String AUTHORIZATION = "authorization";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    public List<StationResponse> getStations() throws Exception {
        String result = parse(request(get("/stations"), null));
        return Arrays.asList(objectMapper.readValue(result, StationResponse[].class));
    }

    public void deleteStation(Long id) throws Exception {
        request(delete("/stations/{id}", id));
    }

    public LineDetailResponse getLine(Long id) throws Exception {
        String result = parse(request(get("/lines/{id}", id)));
        return objectMapper.readValue(result, LineDetailResponse.class);
    }

    public void updateLine(Long id, LocalTime startTime, LocalTime endTime) throws Exception {
        LineRequest dto = new LineRequest(null, startTime, endTime, 10);
        request(put("/lines/{id}", id), dto);
    }

    public List<LineResponse> getLines() throws Exception {
        String result = parse(request(get("/lines")));
        return Arrays.asList(objectMapper.readValue(result, LineResponse[].class));
    }

    public void deleteLine(Long id) throws Exception {
        request(delete("/lines/{id}", id));
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId) throws Exception {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    public void addLineStation(
        Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration
    ) throws Exception {
        LineStationCreateRequest dto = new LineStationCreateRequest(preStationId, stationId, distance, duration);
        request(post("/lines/{id}/stations", lineId), dto);
    }

    public void removeLineStation(Long lineId, Long stationId) throws Exception {
        request(delete("/lines/{lineId}/stations/{stationId}", lineId, stationId));
    }

    public WholeSubwayResponse retrieveWholeSubway() throws Exception {
        String result = parse(request(get("/lines/detail")));
        return objectMapper.readValue(result, WholeSubwayResponse.class);
    }

    public PathResponse findPath(String source, String target, String type) throws Exception {
        String result = parse(request(get("/paths")
            .param("source", source)
            .param("target", target)
            .param("type", type)));
        return objectMapper.readValue(result, PathResponse.class);
    }

    /**
     * 강남 - 역삼 - 선릉
     * |           |
     * |          한티
     * |           |
     * 양재 - 매봉 - 도곡
     */
    public void initStation() throws Exception {
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

    public StationResponse createStation(String name) throws Exception {
        StationCreateRequest dto = new StationCreateRequest(name);
        String result = parse(request(post("/stations"), dto));
        return objectMapper.readValue(result, StationResponse.class);
    }

    public LineResponse createLine(String name) throws Exception {
        LineRequest dto = new LineRequest(name, LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        String result = parse(request(post("/lines"), dto));
        return objectMapper.readValue(result, LineResponse.class);
    }

    public TokenResponse login(String email, String password) throws Exception {
        LoginRequest dto = new LoginRequest(email, password);
        String result = parse(request(post("/oauth/token"), dto));
        return objectMapper.readValue(result, TokenResponse.class);
    }

    public ErrorResponse loginError(String email, String password) throws Exception {
        LoginRequest dto = new LoginRequest(email, password);
        String result = parse(request(post("/oauth/token"), dto));
        return objectMapper.readValue(result, ErrorResponse.class);
    }

    public String createMember(String email, String name, String password) throws Exception {
        MemberRequest dto = new MemberRequest(email, name, password);
        return request(post("/members"), dto)
            .getResponse()
            .getHeader("Location");
    }

    public MemberResponse getMember(String email, TokenResponse tokenResponse) throws Exception {
        String result = parse(request(get("/members")
            .param("email", email)
            .header(AUTHORIZATION, tokenResponse)));
        return objectMapper.readValue(result, MemberResponse.class);
    }

    public ErrorResponse getMemberError(String email, TokenResponse tokenResponse) throws Exception {
        String result = parse(request(get("/members")
            .param("email", email)
            .header(AUTHORIZATION, tokenResponse)));
        return objectMapper.readValue(result, ErrorResponse.class);
    }

    public void updateMemberWithAuthentication(
        MemberResponse memberResponse,
        TokenResponse tokenResponse,
        UpdateMemberRequest updateMemberRequest
    ) throws Exception {
        request(put("/members/{id}",
            memberResponse.getId()).header(AUTHORIZATION, tokenResponse),
            updateMemberRequest
        );
    }

    public void updateMemberWithoutAuthentication(
        MemberResponse memberResponse,
        UpdateMemberRequest updateMemberRequest
    ) throws Exception {
        request(put("/members/{id}", memberResponse.getId()), updateMemberRequest);
    }

    public void deleteMember(MemberResponse memberResponse, TokenResponse tokenResponse) throws Exception {
        request(delete("/members/{id}", memberResponse.getId()).header(AUTHORIZATION, tokenResponse));
    }

    public void createFavorite(FavoriteRequest request, TokenResponse tokenResponse) throws Exception {
        request(post("/favorites").header(AUTHORIZATION, tokenResponse), request);
    }

    public List<FavoriteResponse> getAllFavorites(TokenResponse tokenResponse) throws Exception {
        String result = parse(request(get("/favorites").header(AUTHORIZATION, tokenResponse)));
        return Arrays.asList(objectMapper.readValue(result, FavoriteResponse[].class));
    }

    public void deleteFavorite(Long id, TokenResponse tokenResponse) throws Exception {
        request(delete("/favorites/{id}", id).header(AUTHORIZATION, tokenResponse));
    }

    private String parse(MvcResult mvcResult) throws Exception {
        return mvcResult.getResponse().getContentAsString();
    }

    private MvcResult request(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return request(requestBuilder, null);
    }

    private MvcResult request(MockHttpServletRequestBuilder requestBuilder, Object dto) throws Exception {
        String body = objectMapper.writeValueAsString(dto);
        return mockMvc.perform(requestBuilder
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andDo(print())
            .andReturn();
    }
}

