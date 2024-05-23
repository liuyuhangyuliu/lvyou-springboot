package edu.hit;


import edu.hit.customRealm.PasswordRealm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
public class ShiroConfig {

    @Autowired
    private PasswordRealm passwordRealm;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/api/user/login","anon");

        //默认认证界面路径---当认证不通过时跳转
        shiroFilterFactoryBean.setLoginUrl("/api/user/login");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }



    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();


        //对密码md5处理
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1024);
        passwordRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        Collection<Realm> realms = new ArrayList<>();



        realms.add(passwordRealm);

        defaultWebSecurityManager.setRealms(realms);
        defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager());

        SecurityUtils.setSecurityManager(defaultWebSecurityManager);
//        ThreadContext.bind(defaultWebSecurityManager);

        return defaultWebSecurityManager;
    }

    @Bean
    public CookieRememberMeManager cookieRememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setHttpOnly(true);
        simpleCookie.setName("rememberMe");
        simpleCookie.setMaxAge(7*24*60*60);
        cookieRememberMeManager.setCookie(simpleCookie);
        return cookieRememberMeManager;
    }
}
