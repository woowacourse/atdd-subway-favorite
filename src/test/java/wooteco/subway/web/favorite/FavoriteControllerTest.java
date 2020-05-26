package wooteco.subway.web.favorite;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import wooteco.subway.domain.favorite.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.favorite.FavoriteService;
import wooteco.subway.service.favorite.dto.FavoriteRequest;
import wooteco.subway.service.favorite.dto.FavoriteResponse;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.web.member.AuthorizationExtractor;

@Import(HttpEncodingAutoConfiguration.class)
@WebMvcTest(value = {FavoriteController.class, AuthorizationExtractor.class})
public class FavoriteControllerTest {

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static final String EMAIL = "chomily@woowahan.com";
    static final String NAME = "chomily";
    static final String PASSWORD = "chomily1234";
    static final Long ID = 1L;
    static final String TOKEN = "You are authorized!";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("즐겨찾기를 추가하는 기능 테스트")
    @Test
    void createFavorite() throws Exception {
        Station preStation = new Station("강남역");
        Station station = new Station("선릉역");

        when(jwtTokenProvider.getSubject(anyString())).thenReturn(EMAIL);
        when(memberService.findMemberByEmail(EMAIL)).thenReturn(
            new Member(ID, EMAIL, NAME, PASSWORD));
        when(favoriteService.createFavorite(any(Member.class),
            any(FavoriteRequest.class))).thenReturn(
            new Favorite(1L, preStation.getId(), station.getId()));

        FavoriteRequest favoriteRequest = new FavoriteRequest(preStation.getId(), station.getId());
        String request = OBJECT_MAPPER.writeValueAsString(favoriteRequest);

        mockMvc.perform(post("/members/favorites")
            .contentType(MediaType.APPLICATION_JSON)
            .header("authorization", "bearer" + TOKEN)
            .content(request))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @DisplayName("즐겨찾기 목록을 조회하는 기능 테스트")
    @Test
    void readFavorites() throws Exception {
        Station gangnam = new Station(1L, "강남역");
        Station seolleung = new Station(2L, "선릉역");
        Station yeoksam = new Station(3L, "역삼역");

        FavoriteResponse favorite1 = new FavoriteResponse(gangnam, seolleung);
        FavoriteResponse favorite2 = new FavoriteResponse(yeoksam, gangnam);

        when(jwtTokenProvider.getSubject(anyString())).thenReturn(EMAIL);
        when(memberService.findMemberByEmail(EMAIL)).thenReturn(
            new Member(ID, EMAIL, NAME, PASSWORD));
        when(favoriteService.getFavorites(any(Member.class)))
            .thenReturn(Arrays.asList(favorite1, favorite2));

        MvcResult mvcResult = mockMvc.perform(get("/members/favorites")
            .header("authorization", "bearer" + TOKEN)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<FavoriteResponse> responses = OBJECT_MAPPER.readValue(contentAsString,
            OBJECT_MAPPER.getTypeFactory()
                .constructCollectionType(List.class, FavoriteResponse.class));

        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getPreStation().getId()).isEqualTo(gangnam.getId());
        assertThat(responses.get(0).getStation().getId()).isEqualTo(seolleung.getId());
        assertThat(responses.get(1).getPreStation().getId()).isEqualTo(yeoksam.getId());
        assertThat(responses.get(1).getStation().getId()).isEqualTo(gangnam.getId());
    }
}

