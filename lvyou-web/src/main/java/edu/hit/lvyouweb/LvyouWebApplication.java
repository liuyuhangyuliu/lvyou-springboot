package edu.hit.lvyouweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = "edu.hit")
@MapperScan(basePackages = "edu.hit.mapper")
public class LvyouWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LvyouWebApplication.class, args);
    }

}
