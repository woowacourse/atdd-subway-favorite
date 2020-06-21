package woowa.bossdog.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.bossdog.subway.service.Member.dto.UpdateMemberRequest;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("회원 정보 수정")
    @Test
    void update() {
        // given
        Member member = new Member(111L, "test@test.com", "bossdog", "test");
        UpdateMemberRequest request = new UpdateMemberRequest("changedName", "changedPassword");

        // when
        member.update(request);

        // then
        assertThat(member.getName()).isEqualTo("changedName");
        assertThat(member.getPassword()).isEqualTo("changedPassword");
    }

    @DisplayName("비밀번호 확인")
    @Test
    void checkPassword() {
        // given
        Member member = new Member(111L, "test@test.com", "bossdog", "test");

        // when
        final boolean result = member.checkPassword("test");

        // then
        assertThat(result).isTrue();
    }

}