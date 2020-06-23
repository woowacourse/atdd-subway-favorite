package woowa.bossdog.subway.web.path;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import woowa.bossdog.subway.domain.Station;
import woowa.bossdog.subway.service.path.PathService;
import woowa.bossdog.subway.service.path.dto.PathResponse;
import woowa.bossdog.subway.service.path.dto.PathType;
import woowa.bossdog.subway.service.station.dto.StationResponse;

import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PathApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private PathService pathService;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("경로 조회")
    @Test
    void findPath() throws Exception {
        // given
        String source = "강남역";
        String target = "서울역";
        String type = PathType.DISTANCE.name();
        final List<StationResponse> stations = Lists.newArrayList(
                StationResponse.from(new Station(1L, "강남역")),
                StationResponse.from(new Station(2L, "선릉역")),
                StationResponse.from(new Station(3L, "사당역")),
                StationResponse.from(new Station(4L, "서울역")));
        final PathResponse response = new PathResponse(stations, 30, 30);
        given(pathService.findPath(any())).willReturn(response);

        // when
        mvc.perform(get("/paths?source?=" + source + "&target=" + target + "&type=" + type))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1,\"name\":\"강남역\"")))
                .andExpect(content().string(containsString("\"id\":2,\"name\":\"선릉역\"")))
                .andExpect(content().string(containsString("\"id\":3,\"name\":\"사당역\"")))
                .andExpect(content().string(containsString("\"id\":4,\"name\":\"서울역\"")))
                .andExpect(content().string(containsString("\"distance\":30,\"duration\":30")));

        // then
        verify(pathService).findPath(any());
    }

}