package edu.hit.lvyoubackend;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ServletComponentScan(basePackages = "edu.hit.lvyoubackend.filter")
@MapperScan(basePackages = "edu.hit.lvyoubackend.mapper")
public class LvyouBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LvyouBackendApplication.class, args);
    }

}
