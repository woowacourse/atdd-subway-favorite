package wooteco.subway.web.member.interceptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRequestTypeTest {

    @DisplayName("회원가입 요청 시 토큰 검증 불필요")
    @Test
    void hasNoToken1() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.POST.name(), "/join")).isTrue();
    }

    @DisplayName("로그인 요청 시 토큰 검증 불필요")
    @Test
    void hasNoToken2() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.POST.name(), "/login")).isTrue();
    }

    @DisplayName("회원정보 요청 시 토큰 검증 필요")
    @Test
    void hasNoToken3() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.GET.name(), "/members")).isFalse();
    }

    @DisplayName("회원정보 업데이트 요청 시 토큰 검증 필요")
    @Test
    void hasNoToken4() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.PUT.name(), "/members/1")).isFalse();
    }

    @DisplayName("회원정보 삭제 요청 시 토큰 검증 필요")
    @Test
    void hasNoToken5() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.DELETE.name(), "/members/1")).isFalse();
    }

    @DisplayName("로그인 후 즐겨찾기 추가 요청 시 토큰 검증 필요")
    @Test
    void hasNoToken6() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.POST.name(), "/members/favorites")).isFalse();
    }

    @DisplayName("로그인 후 즐겨찾기 조회기 요청 시 토큰 검증 필요")
    @Test
    void hasNoToken7() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.GET.name(), "/members/favorites")).isFalse();
    }

    @DisplayName("로그인 후 즐겨찾기 삭제 요청 시 토큰 검증 필요")
    @Test
    void hasNoToken8() {
        assertThat(MemberRequestType.hasNoToken(HttpMethod.DELETE.name(), "/members/favorites/1")).isFalse();
    }
}