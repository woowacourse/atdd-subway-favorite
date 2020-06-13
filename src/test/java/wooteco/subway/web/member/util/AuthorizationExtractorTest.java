package wooteco.subway.web.member.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationExtractorTest {

    @DisplayName("bearer Extractor 성공")
    @Test
    void extract() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String authorizationType = "Bearer";
        request.addHeader("Authorization", authorizationType + " token value");

        AuthorizationExtractor authorizationExtractor = new AuthorizationExtractor();

        assertThat(authorizationExtractor.extract(request, authorizationType)).isEqualTo("token value");
    }

    @DisplayName("헤더가 존재하지 않음")
    @Test
    void extractFail() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String authorizationType = "Bearer";

        AuthorizationExtractor authorizationExtractor = new AuthorizationExtractor();

        assertThat(authorizationExtractor.extract(request, authorizationType)).isEqualTo("");
    }
}