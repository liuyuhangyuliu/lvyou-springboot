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
