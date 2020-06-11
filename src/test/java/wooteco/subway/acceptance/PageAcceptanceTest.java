package wooteco.subway.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class PageAcceptanceTest extends AcceptanceTest {

    @Test
    void linePage() throws Exception {
        createLine("신분당선");

        mockMvc.perform(get("/lines")
            .accept(MediaType.TEXT_HTML_VALUE))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
