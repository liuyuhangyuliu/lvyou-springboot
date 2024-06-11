package edu.hit.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;

public class MailUtil {
    public static String sendMail(String content,String ... mailAddress){
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("2196933343@qq.com");
        account.setUser("2196933343@qq.com");
        account.setPass("ydzmtksdtmjeeaac"); //密码
        return cn.hutool.extra.mail.MailUtil.send(account, CollUtil.newArrayList(mailAddress), "测试", content, false);
    }
}
