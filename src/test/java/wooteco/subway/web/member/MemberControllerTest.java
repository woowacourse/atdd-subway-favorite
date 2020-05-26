package wooteco.subway.web.member;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.member.Favorite;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteResponse;
import wooteco.subway.service.station.StationService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    private Member member;
    private String token;

    @MockBean
    private StationRepository stationRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .build();

        member = new Member(1L, TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD);
        token = "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImJyb3duQGVtYWlsLmNvbSJ9.elpAi00vJm751cMJmTLehSXD4-jHHIyHGaAcTSh3jCQ";

    }

    @Test
    void create() throws Exception {
        given(memberService.createMember(any())).willReturn(member);

        String inputJson = "{\"email\":\"" + TEST_USER_EMAIL + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MemberDocumentation.createMember());
    }

    @Test
    void getMember() throws Exception {
        given(memberService.findMemberByEmail(anyString())).willReturn(member);
        given(jwtTokenProvider.nonValidToken(anyString())).willReturn(false);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);

        this.mockMvc.perform(get("/members")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.getMember());
    }

    @Test
    void update() throws Exception {
        given(jwtTokenProvider.nonValidToken(anyString())).willReturn(false);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        String inputJson = "{\"name\":\"" + "brown2" + "\"," +
            "\"password\":\"" + "1234" + "\"" + "}";

        this.mockMvc.perform(put("/members/1")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.updateMember());
    }

    @Test
    void deleteMember() throws Exception {
        given(jwtTokenProvider.nonValidToken(anyString())).willReturn(false);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);

        this.mockMvc.perform(delete("/members/1")
            .header("Authorization", token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MemberDocumentation.deleteMember());
    }

    @Test
    void addFavorite() throws Exception {
        given(jwtTokenProvider.nonValidToken(anyString())).willReturn(false);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);

        String inputJson = "{\"source\":\"" + "강남" + "\"," +
            "\"target\":\"" + "잠실" + "\"" + "}";

        this.mockMvc.perform(post("/members/favorites")
            .header("Authorization", token)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.addFavorite());
    }

    @Test
    void getFavorites() throws Exception {
        Set<FavoriteResponse> favorites = new LinkedHashSet<>();
        favorites.add(new FavoriteResponse(1L, "잠실", "강남"));
        given(memberService.findFavorites(any())).willReturn(favorites);

        this.mockMvc.perform(get("/members/favorites")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.getFavorites());
    }

    @Test
    void deleteFavorites() throws Exception {
        given(memberService.findMemberByEmail(anyString())).willReturn(member);
        given(jwtTokenProvider.nonValidToken(anyString())).willReturn(false);
        given(jwtTokenProvider.getSubject(anyString())).willReturn(TEST_USER_EMAIL);
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/members/favorites/{id}", 1)
            .header("Authorization", token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MemberDocumentation.deleteFavorites());
    }
}
