package learn.java.testrepository.spring.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CacheableClass {

    // 애너테이션으로 스프링 메서드 캐시를 적용한다
    @Cacheable(value = "find")
    public String findCache(final String name) {
        return "found cache - " + name;
    }

    // 애너테이션으로 스프링 메서드 캐시를 갱신한다
    @CachePut(value = "find")
    public String updateCache(final String name) {
        return "update cache - " + name;
    }

    // 애너테이션으로 스프링 메서드 캐시를 제거한다
    @CacheEvict(value = "find", allEntries = true)
    public void clearCache() {
    }

    // 커스텀 키 값으로 메서드 캐싱을 적용한다
    @Cacheable(value = "key", key = "#name")
    public String findCacheWithCustomKey(final String name, final boolean isNew, final boolean isGood) {
        return "found cache - " + name + " with key " + name;
    }

    // 커스텀 키 제너레이터로 메서드 캐싱을 적용한다
    @Cacheable(value = "key-generator", keyGenerator = "customKeyGenerator")
    public String findCacheWithKeyGenerator(final String name, final boolean isNew, final boolean isGood) {
        return "found cache - " + name + " with key generator";
    }

    // 캐시에 접근하는 동안 캐시 엔트리를 잠근다
    @Cacheable(value = "sync", sync = true)
    public String findSyncCache(final String name) {
        return "found cache - " + name;
    }

    // 조건을 만족할 때만 메서드 캐싱을 적용한다
    @Cacheable(value = "condition", condition = "#name.length() >= 5")
    public String findCacheByCondition(final String name) {
        return "found cache - " + name;
    }

    // 조건을 만족할 때만 메서드 캐싱을 적용한다
    // 메서드 호출이 끝난 후 SpEL 을 평가하므로 result 에 접근 가능하다
    @Cacheable(value = "unless", unless = "#result?.length() >= 20")
    public String findCacheWithUnless(final String name) {
        return "found cache - " + name;
    }
}
