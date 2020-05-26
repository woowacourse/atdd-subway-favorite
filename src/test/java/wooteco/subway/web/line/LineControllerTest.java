package wooteco.subway.web.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.config.ETagHeaderFilter;
import wooteco.subway.doc.LineDocumentation;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(ETagHeaderFilter.class)
public class LineControllerTest {
    @MockBean
    private LineService lineService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("eTag를 활용한 HTTP 캐시 설정 검증")
    @Test
    void ETag() throws Exception {
        WholeSubwayResponse response = WholeSubwayResponse.from(Arrays.asList(createMockResponse(), createMockResponse()));
        given(lineService.findLinesWithStations()).willReturn(response);

        String uri = "/lines/detail";

        MvcResult mvcResult = mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("ETag"))
                .andReturn();

        String eTag = mvcResult.getResponse().getHeader("ETag");

        mockMvc.perform(get(uri).header("If-None-Match", eTag))
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(header().exists("ETag"))
                .andReturn();
    }

    private LineDetailResponse createMockResponse() {
        List<Station> stations = Arrays.asList(new Station(), new Station(), new Station());
        return LineDetailResponse.of(new Line(), stations);
    }

    @Test
    public void create() throws Exception {
        final Line line = new Line(
                63L, "신분당선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        when(lineService.save(any())).thenReturn(line);

        mockMvc.perform(post("/lines")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"신분당선\",\"startTime\":\"05:30\"," +
                        "\"endTime\":\"23:30\",\"intervalTime\":10}"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(LineDocumentation.createLine());
    }

    @Test
    public void getAll() throws Exception {
        final List<Line> lines = new ArrayList<>();
        lines.add(new Line(
                63L, "신분당선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10,
                LocalDateTime.now(), LocalDateTime.now()));
        when(lineService.findLines()).thenReturn(lines);

        mockMvc.perform(get("/lines"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LineDocumentation.getLines());
    }

    @Test
    public void getLine() throws Exception {
        final LineDetailResponse line = new LineDetailResponse(
                63L, "신분당선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10,
                LocalDateTime.now(), LocalDateTime.now(), Arrays.asList(new Station(1L, "강남역")));

        when(lineService.getLine(line.getId())).thenReturn(line);

        mockMvc.perform(get("/lines/" + line.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LineDocumentation.getLine());
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(put("/lines/63")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"신분당선\",\"startTime\":\"07:30\"," +
                        "\"endTime\":\"23:59\",\"intervalTime\":20}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LineDocumentation.updateLine());
    }

    @Test
    public void deleteLine() throws Exception {
        mockMvc.perform(delete("/lines/63"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LineDocumentation.deleteLine());
    }
}

