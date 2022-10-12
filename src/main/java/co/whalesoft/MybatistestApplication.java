package co.whalesoft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
//@MapperScan(basePackages = "co.whalesoft.*")
public class MybatistestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatistestApplication.class, args);
    }
}
