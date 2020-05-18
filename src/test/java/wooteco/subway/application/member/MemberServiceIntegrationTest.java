package wooteco.subway.application.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.member.Member;
import wooteco.subway.domain.member.MemberRepository;
import wooteco.subway.service.member.MemberService;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("/truncate.sql")
public class MemberServiceIntegrationTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown";

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void updateMember() {
        Member member = memberRepository.save(new Member(TEST_USER_EMAIL, TEST_USER_NAME, TEST_USER_PASSWORD));
        UpdateMemberRequest request = new UpdateMemberRequest("NEW_" + TEST_USER_NAME, "");

        memberService.updateMember(member.getId(), request);

        Member resultMember = memberRepository.findById(member.getId()).orElseThrow(RuntimeException::new);
        assertThat(resultMember.getName()).isEqualTo("NEW_" + TEST_USER_NAME);
        assertThat(resultMember.getPassword()).isEqualTo(TEST_USER_PASSWORD);
    }
}
