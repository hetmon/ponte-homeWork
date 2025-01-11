package hu.ponte.homework.pontevotehomework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "hu.ponte.homework.pontevotehomework")
@EnableScheduling
public class PonteVoteHomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(PonteVoteHomeworkApplication.class, args);
    }

}
