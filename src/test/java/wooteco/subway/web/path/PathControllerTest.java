package wooteco.subway.web.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.domain.path.PathType;
import wooteco.subway.service.path.DuplicatedStationException;
import wooteco.subway.service.path.NotExistedPathException;
import wooteco.subway.service.path.NotExistedStationException;
import wooteco.subway.service.path.PathService;
import wooteco.subway.service.path.dto.PathResponse;
import wooteco.subway.service.station.dto.StationResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/truncate.sql")
class PathControllerTest {

    @MockBean
    private PathService pathService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("경로 조회")
    @Test
    public void findPath() throws Exception {
        final String source = "GangNam";
        final String target = "JamSil";
        final PathType type = PathType.DISTANCE;

        final List<StationResponse> stations = Arrays.asList(
                new StationResponse(1L, "GangNam", LocalDateTime.now()),
                new StationResponse(5L, "SamSung", LocalDateTime.now()),
                new StationResponse(7L, "Jamsil", LocalDateTime.now())
        );
        final PathResponse pathResponse = new PathResponse(stations, 100, 100);
        given(pathService.findPath(anyString(), anyString(), any())).willReturn(pathResponse);

        this.mockMvc.perform(get("/paths?source=" + source + "&target=" + target + "&type=" + type.name()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("\"id\":1,\"name\":\"GangNam\"")
                ))
                .andExpect(content().string(
                        containsString("\"id\":5,\"name\":\"SamSung\"")
                ))
                .andExpect(content().string(
                        containsString("\"id\":7,\"name\":\"Jamsil\"")
                ))
                .andExpect(content().string(
                        containsString("\"duration\":100,\"distance\":100")
                ));
    }

    @DisplayName("경로가 존재하지 않는 경우")
    @Test
    public void findPathWithInvalidAttributes() throws Exception {
        final String source = "GangNam";
        final String target = "JamSil";
        final PathType type = PathType.DISTANCE;

        given(pathService.findPath(anyString(), anyString(), any())).willThrow(NotExistedPathException.class);

        this.mockMvc.perform(get("/paths?source=" + source + "&target=" + target + "&type=" + type.name()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    public void findPathWithDuplicatedAttributes() throws Exception {
        final String source = "GangNam";
        final String target = "GangNam";
        final PathType type = PathType.DISTANCE;

        given(pathService.findPath(anyString(), anyString(), any())).willThrow(DuplicatedStationException.class);

        this.mockMvc.perform(get("/paths?source=" + source + "&target=" + target + "&type=" + type.name()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("출발역이 존재하지 않는 역일 경우")
    @Test
    public void findPathWithNotExistedSourceStation() throws Exception {
        final String source = "England";
        final String target = "GangNam";
        final PathType type = PathType.DISTANCE;

        given(pathService.findPath(anyString(), anyString(), any())).willThrow(NotExistedStationException.class);

        this.mockMvc.perform(get("/paths?source=" + source + "&target=" + target + "&type=" + type.name()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("도착역이 존재하지 않는 역일 경우")
    @Test
    public void findPathWithNotExistedTargetStation() throws Exception {
        final String source = "GangNam";
        final String target = "Spain";
        final PathType type = PathType.DISTANCE;

        given(pathService.findPath(anyString(), anyString(), any())).willThrow(NotExistedStationException.class);

        this.mockMvc.perform(get("/paths?source=" + source + "&target=" + target + "&type=" + type.name()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}