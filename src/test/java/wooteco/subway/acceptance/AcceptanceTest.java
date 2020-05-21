package wooteco.subway.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.LineRequest;
import wooteco.subway.service.line.dto.LineResponse;
import wooteco.subway.service.line.dto.LineStationCreateRequest;
import wooteco.subway.service.line.dto.WholeSubwayResponse;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.TokenResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationCreateRequest;
import wooteco.subway.service.station.dto.StationResponse;

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
        String result = mockMvc.perform(get("/stations"))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();
        return Arrays.asList(objectMapper.readValue(result, StationResponse[].class));
    }

    public void deleteStation(Long id) throws Exception {
        mockMvc.perform(delete("/stations/{id}", id))
            .andDo(print())
            .andReturn();
    }

    public LineDetailResponse getLine(Long id) throws Exception {
        String result = mockMvc.perform(get("/lines/{id}", id))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();
        return objectMapper.readValue(result, LineDetailResponse.class);
    }

    public void updateLine(Long id, LocalTime startTime, LocalTime endTime) throws Exception {
        String body = objectMapper.writeValueAsString(new LineRequest(null, startTime, endTime, 10));

        mockMvc.perform(put("/lines/{id}", id)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andDo(print())
            .andReturn();
    }

    public List<LineResponse> getLines() throws Exception {
        String result = mockMvc.perform(get("/lines"))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return Arrays.asList(objectMapper.readValue(result, LineResponse[].class));
    }

    public void deleteLine(Long id) throws Exception {
        mockMvc.perform(delete("/lines/{id}", id))
            .andDo(print())
            .andReturn();
    }

    public void addLineStation(Long lineId, Long preStationId, Long stationId) throws Exception {
        addLineStation(lineId, preStationId, stationId, 10, 10);
    }

    public void addLineStation(
        Long lineId, Long preStationId, Long stationId, Integer distance, Integer duration
    ) throws Exception {
        String body = objectMapper.writeValueAsString(
            new LineStationCreateRequest(preStationId, stationId, distance, duration));

        mockMvc.perform(post("/lines/{id}/stations", lineId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andDo(print())
            .andReturn();
    }

    public void removeLineStation(Long lineId, Long stationId) throws Exception {
        mockMvc.perform(delete("/lines/{lineId}/stations/{stationId}", lineId, stationId)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print());
    }

    public WholeSubwayResponse retrieveWholeSubway() throws Exception {
        String result = mockMvc.perform(get("/lines/detail")
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return objectMapper.readValue(result, WholeSubwayResponse.class);
    }

    public PathResponse findPath(String source, String target, String type) throws Exception {
        String result = mockMvc.perform(get("/paths")
            .param("source", source)
            .param("target", target)
            .param("type", type)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

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
        String body = objectMapper.writeValueAsString(new StationCreateRequest(name));

        String result = mockMvc.perform(post("/stations")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return objectMapper.readValue(result, StationResponse.class);
    }

    public LineResponse createLine(String name) throws Exception {
        String body = objectMapper.writeValueAsString(
            new LineRequest(name, LocalTime.of(5, 30), LocalTime.of(23, 30), 10));

        String result = mockMvc.perform(post("/lines")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return objectMapper.readValue(result, LineResponse.class);
    }

    public TokenResponse login(String email, String password) throws Exception {
        String body = objectMapper.writeValueAsString(new LoginRequest(email, password));

        String result = mockMvc.perform(post("/oauth/token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getContentAsString();

        return objectMapper.readValue(result, TokenResponse.class);
    }

    public String createMember(String email, String name, String password) throws Exception {
        String body = objectMapper.writeValueAsString(new MemberRequest(email, name, password));

        return mockMvc.perform(post("/members")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andDo(print())
            .andReturn()
            .getResponse()
            .getHeader("Location");
    }

    public MemberResponse getMember(String email, TokenResponse tokenResponse) throws Exception {
        MvcResult result = mockMvc.perform(get("/members")
            .param("email", email)
            .header(AUTHORIZATION, tokenResponse)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andReturn();

        return mapToMemberResponse(result);
    }

    // TODO: 2020/05/21 생각해보기
    private MemberResponse mapToMemberResponse(final MvcResult result) throws Exception {
        try {
            return objectMapper.readValue(result.getResponse().getContentAsString(), MemberResponse.class);
        } catch (MismatchedInputException e) {
            return null;
        }
    }

    public void updateMemberWithAuthentication(
        MemberResponse memberResponse,
        TokenResponse tokenResponse,
        UpdateMemberRequest updateMemberRequest
    ) throws Exception {
        String updateJsonInfo = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(put("/members/{id}", memberResponse.getId())
            .header(AUTHORIZATION, tokenResponse)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(updateJsonInfo))
            .andDo(print())
            .andReturn();
    }

    public void updateMemberWithoutAuthentication(
        MemberResponse memberResponse,
        UpdateMemberRequest updateMemberRequest
    ) throws Exception {
        String body = objectMapper.writeValueAsString(updateMemberRequest);

        mockMvc.perform(put("/members/{id}", memberResponse.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(body))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    public void deleteMember(MemberResponse memberResponse, TokenResponse tokenResponse) throws Exception {
        mockMvc.perform(delete("/members/{id}", memberResponse.getId())
            .header(AUTHORIZATION, tokenResponse)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andReturn();
    }
}

