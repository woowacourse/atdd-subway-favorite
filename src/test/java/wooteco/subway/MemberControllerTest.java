package wooteco.subway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.subway.config.ETagHeaderFilter;
import wooteco.subway.service.member.MemberService;

@Import(ETagHeaderFilter.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {
    @MockBean
    private MemberService memberService;

    @Autowired
    protected MockMvc mockMvc;

}
