package edu.hit.lvyoubackend.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import org.springframework.stereotype.Component;


@Component
public class MailUtil {
    public static void sendMail(String content,String ... mailAddress){
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        //account.setSslEnable(true);
        account.setPort(587);
        account.setAuth(true);
        account.setFrom("2196933343@qq.com");
        account.setUser("2196933343@qq.com");
        account.setPass("ydzmtksdtmjeeaac"); //密码
        cn.hutool.extra.mail.MailUtil.send(account, CollUtil.newArrayList(mailAddress), "测试", content, false);
    }
}
