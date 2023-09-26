package learn.java.testrepository.plain.stream;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("스트림 컬렉터 학습 테스트")
public class StreamCollectorTest {

    private Map<String, Long> freq;

    @BeforeEach
    void setUp() {
        freq = new HashMap<>();
        freq.put("a", 2L);
        freq.put("b", 10L);
        freq.put("c", 7L);
        freq.put("d", 2L);
        freq.put("e", 12L);
    }

    @Test
    @DisplayName("comparing 메서드는 Comparator 를 생성한다")
    void comparing() {
        // when
        final List<String> topTwo = freq.keySet()
            .stream()
            .sorted(Comparator.comparing(freq::get).reversed()) // value 역순 정렬
            .limit(2)
            .collect(Collectors.toList());

        // then
        assertThat(topTwo).containsExactly("e", "b");
    }

    @Test
    @DisplayName("toMap 은 스트림 원소를 매핑하여 맵을 생성한다")
    void toMap() {
        // when
        final Map<String, Long> result = freq.values()
            .stream()
            .collect(Collectors.toMap(Object::toString, e -> e, (oldVal, newVal) -> 2 * oldVal)); // 충돌이 난 값을 2배로 증가

        // then
        assertThat(result).containsEntry("2", 4L);
    }

    @Test
    @DisplayName("groupingBy 는 동일 키 값을 갖는 원소들을 자료구조로 그룹핑한다")
    void groupingBy() {
        // when
        final Map<String, List<Long>> result = freq.values()
            .stream()
            .collect(Collectors.groupingBy(Object::toString));

        // then
        assertThat(result.get("2")).hasSize(2);
    }

    @Test
    @DisplayName("joining 은 CharSequence 를 연결한다")
    void joining() {
        // when
        final String result = freq.keySet()
            .stream()
            .sorted()
            .limit(2)
            .collect(Collectors.joining(", ", "[", "]"));

        // then
        assertThat(result).isEqualTo("[a, b]");
    }
}
