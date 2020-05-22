package wooteco.subway.web.member;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import wooteco.subway.domain.member.Member;
import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerIntegrationTest {
    private static final Gson gson = new Gson();

    @Autowired
    MemberService memberService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new ShallowEtagHeaderFilter())
                .build();
    }

    @DisplayName("내 정보 확인에서 이메일이 맞지 않을경우 익셉션이 발생한다")
    @Test
    void nonMatchEmailInGetMyInfoTest() throws Exception {
        String anotherEmail = "anotherEmail@gmail.com";

        String token = jwtTokenProvider.createToken(anotherEmail);

        String uri = "/members?email=ramen@gmail.com";

        mockMvc.perform(get(uri)
                .header("authorization", "Bearer" + token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("유저 정보 수정 시 이메일이 맞지 않을경우 익셉션이 발생한다")
    @Test
    void unAuthorizationUpdateRequestTest() throws Exception {
        Member ramen = memberService.createMember(new Member("email@gmail.com", "ramen", "6315"));
        System.out.println(ramen.getId());
        String anotherEmail = "anotherEmail@gmail.com";

        String token = jwtTokenProvider.createToken(anotherEmail);

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("coyle", "6315");
        String updateData = gson.toJson(updateMemberRequest);
        String uri = "/members/1";

        mockMvc.perform(put(uri)
                .header("Authorization", "Bearer" + token)
                .content(updateData)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
