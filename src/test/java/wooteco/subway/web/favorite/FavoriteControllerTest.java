package wooteco.subway.web.favorite;

import com.google.gson.Gson;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import wooteco.subway.DummyTestUserInfo;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.CreateFavoriteRequest;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.exception.NotExistFavoriteDataException;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteControllerTest {
    private static final Gson gson = new Gson();

    @MockBean
    FavoriteService favoriteService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("즐겨찾기 추가한다.")
    @Test
    void createFavoriteTest() throws Exception {
        String sourceStation = "강남역";
        String targetStation = "한티역";

        CreateFavoriteRequest favoriteRequest = new CreateFavoriteRequest(sourceStation, targetStation);
        Member member = new Member(DummyTestUserInfo.EMAIL,DummyTestUserInfo.NAME,DummyTestUserInfo.PASSWORD);

        given(favoriteService.save(any())).willReturn(new Favorite(sourceStation, targetStation, DummyTestUserInfo.EMAIL));
        given(jwtTokenProvider.validateToken(any())).willReturn(true);
        given(jwtTokenProvider.getSubject(any())).willReturn(DummyTestUserInfo.EMAIL);
        given(memberService.findMemberByEmail(any())).willReturn(member);
        given(favoriteService.findFavoriteBySourceAndTarget(any(),any())).willThrow(NotExistFavoriteDataException.class);

        String uri = "/favorites";
        String content = gson.toJson(favoriteRequest);

        mockMvc.perform(post(uri)
                .header("Authorization", "Bearer " + "이메일 Token")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
