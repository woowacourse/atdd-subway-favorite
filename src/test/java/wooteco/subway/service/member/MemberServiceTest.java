package wooteco.subway.service.member;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import wooteco.subway.infra.JwtTokenProvider;
import wooteco.subway.service.member.dto.LoginRequest;
import wooteco.subway.service.member.dto.MemberRequest;
import wooteco.subway.service.member.dto.MemberResponse;
import wooteco.subway.service.member.dto.UpdateMemberRequest;

@Sql("/truncate.sql")
@DataJdbcTest
@Import({MemberService.class, JwtTokenProvider.class})
public class MemberServiceTest {
    public static final String TEST_USER_EMAIL = "brown@email.com";
    public static final String TEST_USER_NAME = "브라운";
    public static final String TEST_USER_PASSWORD = "brown11111111111";
    public static final String SECOND_TEST_USER_EMAIL = "bingbong@email.com";
    public static final String SECOND_TEST_USER_NAME = "bingbong";
    public static final String SECOND_TEST_USER_PASSWORD = "bingbong111111";
    public static Long TEST_USER_ID;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MemberRequest memberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME,
            TEST_USER_PASSWORD);
        MemberResponse memberResponse = memberService.createMember(memberRequest);
        TEST_USER_ID = memberResponse.getId();
    }

    @Test
    void createMember() {
        MemberRequest secondMemberRequest = new MemberRequest(SECOND_TEST_USER_EMAIL,
            SECOND_TEST_USER_NAME,
            SECOND_TEST_USER_PASSWORD);
        MemberResponse memberResponse = memberService.createMember(secondMemberRequest);

        assertThat(memberResponse.getEmail()).isEqualTo(SECOND_TEST_USER_EMAIL);
    }

    @Test
    void findMember() {
        MemberResponse memberResponse = memberService.findMemberById(TEST_USER_ID);

        assertThat(memberResponse.getEmail()).isEqualTo(TEST_USER_EMAIL);
    }

    @Test
    void updateMember() {
        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(SECOND_TEST_USER_NAME,
            SECOND_TEST_USER_PASSWORD);
        memberService.updateMember(TEST_USER_ID, updateMemberRequest);
        MemberResponse memberResponse = memberService.findMemberById(TEST_USER_ID);

        assertThat(memberResponse.getName()).isEqualTo(SECOND_TEST_USER_NAME);
    }

    @Test
    void deleteMember() {
        memberService.deleteMember(TEST_USER_ID);

        assertThatThrownBy(() -> memberService.findMemberById(TEST_USER_ID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void duplicateEmail() {
        MemberRequest sameMemberRequest = new MemberRequest(TEST_USER_EMAIL, TEST_USER_NAME,
            TEST_USER_PASSWORD);

        assertThatThrownBy(() ->
            memberService.createMember(sameMemberRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createToken() {
        LoginRequest loginRequest = new LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD);
        String token = memberService.createToken(loginRequest);

        assertThat(token).isNotNull();
    }
}
