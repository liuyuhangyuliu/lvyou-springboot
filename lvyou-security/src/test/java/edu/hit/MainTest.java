package edu.hit;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    public void test(){
        Md5Hash md5Hash = new Md5Hash("liuyuhang", "567231", 1024);
        System.out.println(md5Hash.toHex());
    }

}