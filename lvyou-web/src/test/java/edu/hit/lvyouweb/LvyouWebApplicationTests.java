package edu.hit.lvyouweb;

import edu.hit.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class LvyouWebApplicationTests {





    @Test
    public void JwtUtilTest(){
        //System.out.println(JwtUtil.getSecret());
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwianRpIjoiNmYxYzgyOGYtZTE0Ni00MTA1LTljZmYtZjhhNjJiMWVkNjYzIiwiZXhwIjoxNzE3NTg0NzU4LCJpYXQiOjE3MTc1ODI5NTgsImlzcyI6ImxpdXl1aGFuZyJ9.q3pwh3J7c9LaCtk2jBK1XolyrbVJtqRGIW9WyLJRfuQ";
//        Claims claims = JwtUtil.parsePayload(token);
//        for(String key:claims.keySet()){
//            System.out.println(key + ":" + claims.get(key));
//        }
        System.out.println(JwtUtil.parsePayload(token).get("username"));
    }

}
