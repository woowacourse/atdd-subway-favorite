package woowa.bossdog.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.bossdog.subway.service.Member.dto.MemberResponse;
import woowa.bossdog.subway.service.Member.dto.UpdateMemberRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberAcceptanceTest extends AcceptanceTest {

    /**
     * 시나리오
     * 1. 회원가입을 한다. (회원 생성)
     * 2. 회원 전체 목록을 불러온다.
     * 3. 회원 단건 조회한다.
     * 4. 회원 정보를 수정한다.
     * 5. 회원을 삭제한다.
     * 6. 데이터 롤백
     */

    @DisplayName("회원 정보 관리")
    @Test
    void manageMember() {
        // 1. 회원 생성
        createMember("test@test.com", "bossdog", "test");
        createMember("test2@test2.com", "bossdog2", "test2");

        // 2. 회원 목록
        List<MemberResponse> responses = listMembers();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getEmail()).isEqualTo("test@test.com");
        assertThat(responses.get(1).getEmail()).isEqualTo("test2@test2.com");

        // 3. 회원 단건 조회
        MemberResponse findMember = findMember(responses.get(0).getId());
        assertThat(findMember.getEmail()).isEqualTo("test@test.com");
        assertThat(findMember.getName()).isEqualTo("bossdog");
        assertThat(findMember.getPassword()).isEqualTo("test");

        // 4. 회원 정보 수정
        UpdateMemberRequest updateRequest = new UpdateMemberRequest("changedName", "changedPassword");
        updateMember(findMember.getId(), updateRequest);
        findMember = findMember(responses.get(0).getId());
        assertThat(findMember.getName()).isEqualTo("changedName");
        assertThat(findMember.getPassword()).isEqualTo("changedPassword");

        // 5. 회원 삭제
        deleteMember(findMember.getId());
        responses = listMembers();
        assertThat(responses).hasSize(1);

        // 6. 데이터 롤백
        deleteMember(responses.get(0).getId());
    }
}
