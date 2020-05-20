package wooteco.subway.web.member;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.domain.member.Member;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.MemberRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    private static final Gson gson = new Gson();

    @MockBean
    MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

    @DisplayName("올바른 값이 입력되면 회원가입이 된다")
    @Test
    void createMemberTest() throws Exception {
        Member member = new Member(1L,"ramen6315@gmail.com","ramen","6315)");
        MemberRequest memberRequest = new MemberRequest("ramen6315@gmail.com","ramen","6315)");
        given(memberService.createMember(any())).willReturn(member);

        String uri = "/members";
        String content = gson.toJson(memberRequest);

        mockMvc.perform(post(uri)
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}
