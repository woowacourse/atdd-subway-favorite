package wooteco.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class AuthenticationAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인후 즐겨찾기 페이지 조회")
    @TestFactory
    Stream<DynamicTest> name() {
        return Stream.of(
                dynamicTest("로그인 후 인증정보를 생성", () -> {

                }),
                dynamicTest("", () -> {

                })
        );

    }
}
