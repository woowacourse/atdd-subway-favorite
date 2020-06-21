package woowa.bossdog.subway.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import woowa.bossdog.subway.domain.Line;

import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LineRepositoryTest {

    @Autowired
    private LineRepository lineRepository;

    @DisplayName("영속화 테스트")
    @Test
    void save_find() {
        // given
        final Line line = new Line("신분당선", LocalTime.of(5, 30), LocalTime.of(23, 30), 10);
        lineRepository.save(line);

        // when
        final Line findLine = lineRepository.findById(line.getId())
                .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(findLine.getId()).isEqualTo(line.getId());
        assertThat(findLine.getName()).isEqualTo(line.getName());
        assertThat(findLine.getStartTime()).isEqualTo(line.getStartTime());
        assertThat(findLine.getEndTime()).isEqualTo(line.getEndTime());
        assertThat(findLine.getIntervalTime()).isEqualTo(line.getIntervalTime());
    }

}