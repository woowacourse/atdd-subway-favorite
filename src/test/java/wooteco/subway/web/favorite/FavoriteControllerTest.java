package wooteco.subway.web.favorite;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {
    private static final Gson gson = new Gson();

//    @MockBean
//    FavoriteService favoriteService;
//
//    @MockBean
//    JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("즐겨찾기 추가한다.")
    @Test
    void createFavoriteTest() throws Exception {
        CreateFavoriteRequest favoriteRequest = new CreateFavoriteRequest("강남역", "한티역");

        String uri = "/favorites";
        String content = gson.toJson(favoriteRequest);

        mockMvc.perform(post(uri)
                .header("Authorization", "Bearer " + "이메일 Token")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MemberDocumentation.createMember())
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
