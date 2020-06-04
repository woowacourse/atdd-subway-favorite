package wooteco.subway.infra;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTokenProviderTest {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void createToken() {
        given(jwtTokenProvider.createToken(any())).willReturn("ACCESS_TOKEN");
        String token = jwtTokenProvider.createToken("token resource");

        assertThat(token).isNotNull();
        assertThat(token).isEqualTo("ACCESS_TOKEN");
    }
}