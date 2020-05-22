package wooteco.subway.web.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.doc.LoginMemberDocumentation;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.FavoriteRequest;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = {"test"})
@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
class LoginMemberControllerTest {

    private static final String EMAIL = "pci2676@gmail.com";
    private static final String NAME = "박찬인";
    private static final String PASSWORD = "1234";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addFilter(new ShallowEtagHeaderFilter())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
        stationRepository.deleteAll();
    }

    @DisplayName("로그인")
    @Test
    void login() throws Exception {
        memberRepository.save(new Member(EMAIL, NAME, PASSWORD));

        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        String loginRequestContent = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(loginRequestContent)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.loginMember());
    }

    @DisplayName("존재하지 않는 이메일로 로그인 시도")
    @Test
    void loginFailEmail() throws Exception {
        LoginRequest loginRequest = new LoginRequest("example@naver.com", PASSWORD);
        String loginRequestContent = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(loginRequestContent)
        )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(LoginMemberDocumentation.loginFailEmail());
    }

    @DisplayName("잘못된 패스워드로 로그인 시도")
    @Test
    void loginFailPassword() throws Exception {
        memberRepository.save(new Member(EMAIL, NAME, PASSWORD));

        LoginRequest loginRequest = new LoginRequest(EMAIL, "PASSWORD");
        String loginRequestContent = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(loginRequestContent)
        )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(LoginMemberDocumentation.loginFailedPassword());
    }

    @DisplayName("자신의 정보를 조회한다.")
    @Test
    void getMyInfo() throws Exception {
        memberRepository.save(new Member(EMAIL, NAME, PASSWORD));

        String token = "bearer asdf.zxcv.qewr";

        mockMvc.perform(get("/me")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(LoginMemberDocumentation.getMyInfo());
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteMyInfo() throws Exception {
        memberRepository.save(new Member(EMAIL, NAME, PASSWORD));

        String token = "bearer asdf.zxcv.qewr";

        mockMvc.perform(delete("/me")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LoginMemberDocumentation.deleteMyInfo());
    }

    @DisplayName("자신의 정보를 갱신한다.")
    @Test
    void updateMyInfo() throws Exception {
        memberRepository.save(new Member(EMAIL, NAME, PASSWORD));

        String token = "bearer asdf.zxcv.qewr";
        UpdateMemberRequest request = new UpdateMemberRequest(NAME, PASSWORD);
        String updateRequestContent = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/me")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content(updateRequestContent)
        )
                .andExpect(status().isNoContent())
                .andDo(print())
                .andDo(LoginMemberDocumentation.updateMyInfo());
    }

    @DisplayName("회원의 즐겨찾는 경로를 추가한다.")
    @Test
    void getMemberFavorite() throws Exception {
        //given
        Member member = memberRepository.save(new Member(EMAIL, NAME, PASSWORD));
        Station source = stationRepository.save(new Station("잠실"));
        Station target = stationRepository.save(new Station("역삼"));

        String token = jwtTokenProvider.createToken(member.getEmail());

        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        String favoriteRequestContent = objectMapper.writeValueAsString(favoriteRequest);

        this.mockMvc.perform(post("/me/favorites")
                .content(favoriteRequestContent)
                .header("Authorization", "bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(LoginMemberDocumentation.createFavorite());
    }

}