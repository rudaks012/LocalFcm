package co.whalesoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication
//public class PushApplication {
//    public static void main(String[] args) {
//        SpringApplication.run(PushApplication.class, args);
//    }
//}

@SpringBootApplication
public class PushApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PushApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(PushApplication.class, args);
    }

}

