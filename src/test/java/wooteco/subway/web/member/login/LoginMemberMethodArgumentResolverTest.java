package wooteco.subway.web.member.login;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.service.member.MemberService;

@SpringBootTest
public class LoginMemberMethodArgumentResolverTest {
    @Autowired
    private MemberService memberService;

    @DisplayName("토큰을 추출한 결과 해당하는 이메일과 일치하는 멤버가 없는 경우 예외가 발생하는지 테스트")
    @Test
    void noMemberTest() {
    }
}
