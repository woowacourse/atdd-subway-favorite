package wooteco.subway.web.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import wooteco.subway.config.ETagHeaderFilter;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.line.LineService;
import wooteco.subway.service.line.dto.LineDetailResponse;
import wooteco.subway.service.line.dto.WholeSubwayResponse;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ETagHeaderFilter.class)
public class LineControllerTest {
    @MockBean
    private LineService lineService;

    @Autowired
    protected MockMvc mockMvc;

    @DisplayName("eTag를 활용한 HTTP 캐시 설정 검증")
    @Test
    void ETag() throws Exception {
        WholeSubwayResponse response = WholeSubwayResponse.of(Arrays.asList(createMockResponse(), createMockResponse()));
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
}

