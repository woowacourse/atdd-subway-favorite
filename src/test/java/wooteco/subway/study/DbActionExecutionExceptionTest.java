package wooteco.subway.study;

import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

class DbActionExecutionExceptionTest {

    @Test
    void isCauseCanBeNull() {
        DbActionExecutionException dbActionExecutionException = new DbActionExecutionException(null, null);
        Throwable cause = dbActionExecutionException.getCause();
        assertThat(cause).isNull();
    }
}
