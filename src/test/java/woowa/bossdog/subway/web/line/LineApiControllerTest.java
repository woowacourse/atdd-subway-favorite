package woowa.bossdog.subway.web.line;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import woowa.bossdog.subway.domain.Line;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.service.line.LineService;
import woowa.bossdog.subway.service.line.dto.LineDetailResponse;
import woowa.bossdog.subway.service.line.dto.LineResponse;
import woowa.bossdog.subway.service.line.dto.WholeSubwayResponse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LineApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private LineService lineService;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void create() throws Exception {
        // given
        final Line line = new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        given(lineService.createLine(any())).willReturn(LineResponse.from(line));

        // when
        mvc.perform(post("/lines")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"2호선\",\"startTime\":\"05:30\",\"endTime\":\"23:30\",\"intervalTime\":10}"))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/lines/" + line.getId()));
        // then
        verify(lineService).createLine(any());
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void list() throws Exception {
        // given
        final List<LineResponse> lines = new ArrayList<>();
        lines.add(LineResponse.from(new Line("2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10)));
        lines.add(LineResponse.from(new Line("3호선", LocalTime.of(5, 50), LocalTime.of(23, 50), 12)));
        given(lineService.listLines()).willReturn(lines);

        // when
        mvc.perform(get("/lines"))
                .andExpect(status().isOk());

        // then
        verify(lineService).listLines();
    }

    @DisplayName("지하철 노선 단건 조회")
    @Test
    void find() throws Exception {
        // given
        final Line line = new Line(63L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        given(lineService.findLine(any())).willReturn(LineResponse.from(line));

        // when
        mvc.perform(get("/lines/" + line.getId()))
                .andExpect(status().isOk());

        // then
        verify(lineService).findLine(eq(63L));
    }

    @DisplayName("지하철 노선 정보 수정")
    @Test
    void update() throws Exception {
        // given
        final Line line = new Line(63L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        // when
        mvc.perform(put("/lines/" + line.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"신분당선\",\"startTime\":\"06:30\",\"endTime\":\"23:30\",\"intervalTime\":15}"))
                .andExpect(status().isOk());

        // then
        verify(lineService).updateLine(eq(63L), any());
    }

    @DisplayName("지하철 역 삭제")
    @Test
    void remove() throws Exception {
        // given
        final Line line = new Line(63L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        // when
        mvc.perform(delete("/lines/" + line.getId()))
                .andExpect(status().isNoContent());

        // then
        verify(lineService).deleteLine(eq(63L));
    }

    @DisplayName("지하철 구간 추가")
    @Test
    void addLineStation() throws Exception {
        // given
        Line line = new Line(10L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);

        // when
        mvc.perform(post("/lines/" + line.getId() + "/stations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"preStationId\":5,\"stationId\":8,\"distance\":10,\"duration\":10}"))
                .andExpect(status().isCreated());

        // then
        verify(lineService).addLineStation(eq(10L), any());
    }

    @DisplayName("지하철 구간 삭제")
    @Test
    void removeLineStation() throws Exception {
        // given
        Line line = new Line(10L, "2호선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        Station station = new Station(5L, "강남역");

        // when
        mvc.perform(delete("/lines/" + line.getId() + "/stations/" + station.getId()))
                .andExpect(status().isNoContent());
        // then
        verify(lineService).deleteLineStation(eq(10L), eq(5L));
    }

    @DisplayName("노선과 구간 조회")
    @Test
    void findLineDetail() throws Exception {
        // given
        List<Station> stations = Lists.newArrayList(new Station(1L, "교대역"), new Station(2L, "고터역"));
        Line line = new Line(2L, "3호선", LocalTime.of(6,30), LocalTime.of(23,0), 10);
        LineDetailResponse response = LineDetailResponse.of(line, stations);
        given(lineService.findLineDetail(any())).willReturn(response);

        // when
        mvc.perform(get("/lines/" + line.getId() + "/stations"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":2,\"name\":\"3호선\",\"startTime\":\"06:30:00\","
                        +"\"endTime\":\"23:00:00\",\"intervalTime\":10,\"stations\":[{\"id\":1,\"name\":\"교대역\"},"
                        +"{\"id\":2,\"name\":\"고터역\"}]}"));

        // then
        verify(lineService).findLineDetail(eq(2L));
    }

    @DisplayName("전체 노선과 구간 조회")
    @Test
    void listLineDetail() throws Exception {
        // given
        List<Station> stations = Lists.newArrayList(new Station(1L, "교대역"), new Station(2L, "고터역"));
        Line line = new Line(2L, "3호선", LocalTime.of(6,30), LocalTime.of(23,0), 10);

        final List<LineDetailResponse> lineDetailResponses = Lists.newArrayList(LineDetailResponse.of(line, stations));
        WholeSubwayResponse response = new WholeSubwayResponse(lineDetailResponses);
        given(lineService.listLineDetail()).willReturn(response);

        // when
        mvc.perform(get("/lines/detail"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"lineDetailResponse\":[{\"id\":2,\"name\":\"3호선\","
                        + "\"startTime\":\"06:30:00\",\"endTime\":\"23:00:00\",\"intervalTime\":10,"
                        + "\"stations\":[{\"id\":1,\"name\":\"교대역\"},{\"id\":2,\"name\":\"고터역\"}]}]}"));

        // then
        verify(lineService).listLineDetail();
    }
}