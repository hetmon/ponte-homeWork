package hu.ponte.homework.pontevotehomework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "makeVoteEventExecutor")
    public Executor getAsyncExecutor() {
        return createThreadPoolTaskExecutor(5, 10, 200);
    }

    private ThreadPoolTaskExecutor createThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize, int queueCapacity) {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setThreadNamePrefix("Async-Executor-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
