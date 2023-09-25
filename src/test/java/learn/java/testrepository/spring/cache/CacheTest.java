package learn.java.testrepository.spring.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("스프링 캐시 학습 테스트")
@SpringBootTest
class CacheTest {

    @Autowired
    CacheableClass cache;

    @BeforeEach
    void setup() {
        cache.clearCache();
    }

    @Test
    @DisplayName("스프링 캐시는 메서드 호출 결과를 캐싱한다")
    void cache_methodResult() {
        // given
        final String key = "hello";

        // when
        final String result = cache.findCache(key);

        // then
        assertThat(result == cache.findCache(key)).isTrue();
    }

    @Test
    @DisplayName("스프링 캐시 값을 갱신할 수 있다")
    void cache_update() {
        // given
        final String key = "hello";
        final String result = cache.findCache(key);

        // when
        final String updateResult = cache.updateCache(key);

        // then
        assertThat(result == cache.findCache(key)).isFalse();
        assertThat(updateResult == cache.findCache(key)).isTrue();
    }

    @Test
    @DisplayName("스프링 캐시를 제거할 수 있다")
    void cache_clear() {
        // given
        final String key = "hello";
        final String result = cache.findCache(key);

        // when
        cache.clearCache();

        // then
        assertThat(result == cache.findCache(key)).isFalse();
    }

    @Test
    @DisplayName("파라미터가 조건을 만족하는 경우에만 캐싱을 적용할 수 있다")
    void cache_conditional() {
        // given
        final String satisfyingKey = "satisfying key";
        final String unsatisfyingKey = "hi";

        // when
        final String cachedResult = cache.findCacheByCondition(satisfyingKey);
        final String uncachedResult = cache.findCacheByCondition(unsatisfyingKey);

        // then
        assertAll(
            () -> assertThat(cachedResult == cache.findCacheByCondition(satisfyingKey)).isTrue(),
            () -> assertThat(uncachedResult == cache.findCacheByCondition(unsatisfyingKey)).isFalse()
        );
    }

    @Test
    @DisplayName("결과 값이 조건을 만족하는 경우에만 캐싱을 적용할 수 있다")
    void cache_unless() {
        // given
        final String firstKey = "hi";
        final String secondKey = "cache unsatisfying very long key";

        // when
        final String cachedResult = cache.findCacheWithUnless(firstKey);
        final String uncachedResult = cache.findCacheWithUnless(secondKey);

        // then
        assertAll(
            () -> assertThat(cachedResult == cache.findCacheWithUnless(firstKey)).isTrue(),
            () -> assertThat(uncachedResult == cache.findCacheWithUnless(secondKey)).isFalse()
        );
    }
}