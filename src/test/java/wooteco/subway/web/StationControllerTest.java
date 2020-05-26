package wooteco.subway.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.StationDocumentation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.station.StationService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class StationControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private StationService stationService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void create() throws Exception {
        final Station station = new Station(11L, "잠실역");
        when(stationService.createStation(any())).thenReturn(station);

        mvc.perform(post("/stations")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"잠실역\"}"))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(StationDocumentation.createStation());
    }

    @Test
    public void getStation() throws Exception {
        final List<Station> stations = new ArrayList<>();
        stations.add(new Station(11L, "잠실역"));
        when(stationService.findStations()).thenReturn(stations);

        mvc.perform(get("/stations"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(StationDocumentation.getStations());
    }

    @Test
    public void deleteStation() throws Exception {

        mvc.perform(delete("/stations/11"))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(StationDocumentation.deleteStation());
    }

}