# 开发者日志
- 2024年5月23日

报错没找到securityManager，看了一下有哪些bean，没有ShiroConfig，看了一下ComponentScan的basePackages，没错啊。最后发现是没引入security模块的依赖，因为是在web模块下做的。

但是我的代码还得加上
``` java
SecurityUtils.setSecurityManager(defaultWebSecurityManager);
```
但我看别人的不用

- 2024年5月26日

今天改了一些maven的错误。首先是循环依赖，这个涉及你模块的设计，没有模块依赖不是说明你解耦做的好，
而是最基本的原则。刚开始我把Response这个类写到了web模块里，而我的Service又得返回这个响应对象，web和service就相互依赖了。
后来把Response放到了common模块里。然后新加了一个parent模块，parent的用法是就放一个pom.xml，把都得用到的像shiro,mybatis放里面
说是parent其实是公共模块，根目录下放的pom是真的一个汇总的，写modules。

parent标签里要写relativePath,不写的话会上中央仓库里找。

controller里必须传httpsession,response header里才有jsessionid


- 2024年5月31日

会话管理还是改用jwt，因为session得结合shiro，但是不会做。综合来看jwt优点多一些，思路也清晰一些。
还考虑过第三方登录，不过很复杂，以后再说。
采用访问令牌+刷新令牌，访问令牌过期时间短，几十分钟，刷新令牌过期时间长，一周或者多少。
用户第一次登陆，服务端响应访问令牌，redis存刷新令牌，以后请求携带访问令牌，如果访问过期，服务端响应错误码，查刷新令牌，获得新的访问令牌并相应。
客户端收到错误码和新的访问令牌，重新发请求。如果刷新令牌过期，服务端返回重新登录。
如果用户登出，前端要清除token，后端要维护访问令牌的黑名单，还要删除用户的刷新令牌。

关于安全性，访问令牌存在客户端有可能被窃取，虽然会设http-only，但是就没有风险吗？https也应该是必须的。
所以设过期时间尽量短，我目前没理解，这些措施只是为了减小而不是杜绝风险吗？还是我对攻击方式认知错误了。
刷新令牌oauth2都是返回给给客户端了，刷新令牌应该安全要求更高，目前没理解怎么保证刷新令牌的安全。
我觉得放redis的思路挺好的，不知道对不对。

- 2024年6月3日
 遇到一个比较复杂的问题，出现在JwtUtil里，我想用@value注解读application.yml的值到secret和过期时间里，
他们都是静态变量，具体参见代码注释
