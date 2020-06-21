package woowa.bossdog.subway.web.station;

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
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.service.station.StationService;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StationApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private StationService stationService;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("지하철 역 생성")
    @Test
    void create() throws Exception {
        // given
        final Station station = new Station("강남역");
        given(stationService.createStation(any())).willReturn(StationResponse.from(station));
        
        // when
        mvc.perform(post("/stations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"강남역\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().stringValues("location", "/stations/" + station.getId()));
        // then
        verify(stationService).createStation(any());
    }

    @DisplayName("지하철 역 목록 조회")
    @Test
    void list() throws Exception {
        // given
        final List<StationResponse> stations = new ArrayList<>();
        stations.add(StationResponse.from(new Station("강남역")));
        stations.add(StationResponse.from(new Station("선릉역")));
        given(stationService.listStations()).willReturn(stations);

        // when
        mvc.perform(get("/stations"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"강남역\"")))
                .andExpect(content().string(containsString("\"name\":\"선릉역\"")));

        // then
        verify(stationService).listStations();
    }

    @DisplayName("지하철 역 단건 조회")
    @Test
    void find() throws Exception {
        // given
        final Station station = new Station(63L, "선릉역");
        given(stationService.findStation(any())).willReturn(StationResponse.from(station));

        // when
        mvc.perform(get("/stations/" + station.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":63,\"name\":\"선릉역\"")));

        // then
        verify(stationService).findStation(eq(63L));
    }

    @DisplayName("지하철 역 삭제")
    @Test
    void remove() throws Exception {
        // given
        final Station station = new Station(63L, "선릉역");

        // when
        mvc.perform(delete("/stations/" + station.getId()))
                .andExpect(status().isNoContent());

        // then
        verify(stationService).deleteStation(eq(63L));
    }

}