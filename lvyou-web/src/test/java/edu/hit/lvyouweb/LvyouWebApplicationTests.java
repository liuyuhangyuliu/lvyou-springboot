package edu.hit.lvyouweb;

import edu.hit.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class LvyouWebApplicationTests {


    @Value("${jwt.secret}")
    public  String secret;

    @Autowired
    private JwtUtil jwtUtil;
    @Test
    public void JwtUtilTest(){
        //System.out.println(JwtUtil.getSecret());
    }

}
