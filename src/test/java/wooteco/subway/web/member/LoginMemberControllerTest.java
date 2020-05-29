package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.favorite.dto.FavoriteResponse;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wooteco.subway.AcceptanceTest.*;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class LoginMemberControllerTest extends MemberApiTest {
    private Member member;
    private String token;
    private List<Favorite> favorites;

    @BeforeEach
    public void setUpFields() {
        favorites = Arrays.asList(new Favorite(1L, 1L, 2L),
                new Favorite(2L, 2L, 3L), new Favorite(3L, 3L, 4L));
        member = new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD, new HashSet<>(favorites));
        token = TEST_TOKEN;
    }

    @Test
    void login() throws Exception {
        given(memberService.createMember(any())).willReturn(member);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
                "\"name\":\"" + TEST_USER_NAME + "\"," +
                "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/oauth/token")
                .header("Authorization", token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.login());
    }

    @Test
    void updateMember() throws Exception {
        String inputJson = "{\"name\":\"" + "sample_name" + "\"," +
                "\"password\":\"" + "sample_password" + "\"}";

        this.mockMvc.perform(put("/me")
                .header("Authorization", token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.updateMember());
    }

    @Test
    void deleteMember() throws Exception {
        this.mockMvc.perform(delete("/me")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.deleteMember());
    }

    @Test
    void createFavorite() throws Exception {
        String inputJson = "{\"source\":\"" + "1" + "\"," +
                "\"target\":\"" + "3" + "\"}";

        this.mockMvc.perform(post("/me/favorites")
                .header("Authorization", token)
                .content(inputJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(MemberDocumentation.createFavorite());
    }

    @Test
    void findAllFavorites() throws Exception {
        List<FavoriteResponse> favoriteResponses = favorites.stream()
                .map(favorite -> new FavoriteResponse(favorite.getId(), favorite.getSourceStationId(), favorite.getTargetStationId()
                        , STATION_NAME_KANGNAM, STATION_NAME_DOGOK))
                .collect(Collectors.toList());

        when(memberService.findAllFavorites(any())).thenReturn(favoriteResponses);

        this.mockMvc.perform(get("/me/favorites")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(MemberDocumentation.findAllFavorites());

    }

    @Test
    void removeFavorite() throws Exception {
        this.mockMvc.perform(delete("/me/favorites/1")
                .header("Authorization", token)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(MemberDocumentation.removeFavorite());
    }
}