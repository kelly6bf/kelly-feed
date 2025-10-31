package site.study.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PositiveIntegerCounterTest {

    @DisplayName("정수 값을 1 증가시킨다.")
    @Test
    void increasePositiveInteger() {
        // Given
        final PositiveIntegerCounter counter = new PositiveIntegerCounter();

        // When
        counter.increase();

        // Then
        assertThat(counter.getCount()).isEqualTo(1);
    }

    @DisplayName("정수 값을 1 감소시킨다.")
    @Test
    void decreasePositiveInteger() {
        // Given
        final PositiveIntegerCounter counter = new PositiveIntegerCounter();
        counter.increase();
        counter.increase();

        // When
        counter.decrease();

        // Then
        assertThat(counter.getCount()).isEqualTo(1);
    }

    @DisplayName("정수 값이 0일때 값을 감소시켜도 0 이하로 내려가지 않는다.")
    @Test
    void notDecreaseUnder0() {
        // Given
        final PositiveIntegerCounter counter = new PositiveIntegerCounter();

        // When
        counter.decrease();

        // Then
        assertThat(counter.getCount()).isEqualTo(0);
    }
}
