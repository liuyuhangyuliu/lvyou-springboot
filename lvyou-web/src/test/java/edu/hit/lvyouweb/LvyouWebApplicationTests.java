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
        String token = JwtUtil.genAccessToken("lyh");
//        Claims claims = JwtUtil.parsePayload(token);
//        for(String key:claims.keySet()){
//            System.out.println(key + ":" + claims.get(key));
//        }
        System.out.println(JwtUtil.parsePayload(token).get("username"));
    }

}
