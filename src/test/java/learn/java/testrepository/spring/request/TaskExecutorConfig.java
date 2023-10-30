package learn.java.testrepository.spring.request;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


// Component Scan 에서 제외되므로 수동으로 테스트 컨텍스트에 등록해줘야 한다
@TestConfiguration
public class TaskExecutorConfig {

    @Bean("callableTaskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(100);
        return taskExecutor;
    }
}
