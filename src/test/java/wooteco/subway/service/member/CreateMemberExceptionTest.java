package wooteco.subway.service.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateMemberExceptionTest {

    @Test
    void constructWithoutCause() {
        CreateMemberException createMemberException = new CreateMemberException("message");
        Throwable cause = createMemberException.getCause();
        assertThat(cause).isNull();
    }
}