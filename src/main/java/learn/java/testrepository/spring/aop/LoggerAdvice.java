package learn.java.testrepository.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggerAdvice {

    // 애너테이션이 부착된 클래스에 대해 실행되는 포인트 컷 정의
    @Pointcut("@annotation(learn.java.testrepository.spring.aop.Logging)")
    public void logging() {}

    @Around("logging()")
    public Object doLogging(ProceedingJoinPoint joinPoint){
        // 타깃 메서드 시작 전 - @Before
        log.info("[" + joinPoint.getSignature() + "] @Before");

        // 타깃 메서드 호출
        try {
            // @Around 는 메서드 실행 흐름을 제어할 수 있다
            Object result = joinPoint.proceed();
            log.info("[" + joinPoint.getSignature() + "] @AfterReturning");
            return result;
        } catch (Throwable e) {
            log.info("[" + joinPoint.getSignature() + "] @AfterThrowing");
            throw new RuntimeException(e);
        } finally {
            log.info("[" + joinPoint.getSignature() + "] @After");
        }
    }

}
