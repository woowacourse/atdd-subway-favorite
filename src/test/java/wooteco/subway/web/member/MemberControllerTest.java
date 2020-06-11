package wooteco.subway.web.member;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wooteco.subway.service.member.MemberServiceTest.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import wooteco.subway.doc.MemberDocumentation;
import wooteco.subway.domain.station.Station;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.station.StationService;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/truncate.sql")
public class MemberControllerTest {
    private String token;

    @Autowired
    private StationService stationService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .addFilter((request, response, chain) -> {
                response.setCharacterEncoding("UTF-8");
                chain.doFilter(request, response);
            }, "/*")
            .build();
        memberService.createMember(new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        token = "bearer " + memberService.createToken(new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD));
    }

    @Test
    void create() throws Exception {
        String inputJson = "{\"email\":\"" + "pobi@email.com" + "\"," +
            "\"name\":\"" + TEST_USER_NAME + "\"," +
            "\"password\":\"" + TEST_USER_PASSWORD + "\"}";

        this.mockMvc.perform(post("/members")
            .content(inputJson)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(MemberDocumentation.createMember())
            .andReturn();
    }

    @Test
    void getMember() throws Exception {
        this.mockMvc.perform(get("/members")
            .header(AuthorizationExtractor.AUTHORIZATION, token))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.getMember());
    }

    @Test
    void update() throws Exception {
        String inputJson = "{\"name\":\"" + "brown2" + "\"," +
            "\"password\":\"" + "1234" + "\"" + "}";

        this.mockMvc.perform(put("/members")
            .header(AuthorizationExtractor.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(inputJson))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.updateMember());
    }

    @Test
    void deleteMember() throws Exception {
        this.mockMvc.perform(delete("/members")
            .header(AuthorizationExtractor.AUTHORIZATION, token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MemberDocumentation.deleteMember());
    }

    @Test
    void addFavorite() throws Exception {
        stationService.createStation(new Station("잠실"));
        stationService.createStation(new Station("강남"));
        String inputJson = "{\"source\":\"" + "강남" + "\"," +
            "\"target\":\"" + "잠실" + "\"" + "}";

        this.mockMvc.perform(post("/members/favorites")
            .header(AuthorizationExtractor.AUTHORIZATION, token)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(inputJson))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.addFavorite());
    }

    @Test
    void getFavorites() throws Exception {
        stationService.createStation(new Station("잠실"));
        stationService.createStation(new Station("강남"));
        memberService.addFavorite(memberService.findMemberByEmail(TEST_USER_EMAIL),
            new FavoriteRequest("잠실", "강남"));

        this.mockMvc.perform(get("/members/1/favorites")
            .header(AuthorizationExtractor.AUTHORIZATION, token))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(MemberDocumentation.getFavorites());
    }

    @Test
    void deleteFavorite() throws Exception {
        stationService.createStation(new Station("잠실"));
        stationService.createStation(new Station("강남"));
        memberService.addFavorite(memberService.findMemberByEmail(TEST_USER_EMAIL),
            new FavoriteRequest("잠실", "강남"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/members/1/favorites/{id}", 1, 1)
            .header(AuthorizationExtractor.AUTHORIZATION, token))
            .andExpect(status().isNoContent())
            .andDo(print())
            .andDo(MemberDocumentation.deleteFavorites());
    }
}