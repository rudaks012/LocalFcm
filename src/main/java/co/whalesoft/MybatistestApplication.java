package co.whalesoft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
@MapperScan(basePackages = "co.whalesoft.*")
public class MybatistestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatistestApplication.class, args);
    }
}
