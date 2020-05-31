package wooteco.subway.web.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wooteco.subway.service.member.MemberService;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginMemberControllerTest {
    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

    }

    @Test
    void createTokenWithError() throws Exception {
        given(memberService.createToken(any())).willThrow(IllegalArgumentException.class);

        String input = "\"email\" : \"brown@email.com\", \"password\" : \"brown\"\n";
        this.mockMvc.perform(post("/oauth/token")
                .content(input)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
