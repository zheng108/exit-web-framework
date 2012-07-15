exit-web-framework
==================

exit-web-framework是对常用的java web开发封装实用功能来提高开发效率。exit-web-framework基于Spring 3、Hibernate4框架
来对做项目核心三层和MVC的管理。使用到的新功能有spring缓存工厂、apeche shiro安全框架、spring mvc 3等主要技术，该项
目分为两个部分做底层的封装，和带一个项目功能演示例子。

1.exit-common
该jar包是对基本的常用工具类的一些简单封装。如泛型，反射，配置文件等工具类的封装。

2.exit-orm
该jar包是对持久化层的框架封装，目前只对Hibernate4的CURD和辅助功能封装。

项目功能演示例子:
在文件夹的shorcase里有一个vcs-admin项目。该项目是对以上两个框架(exit-common和exit-orm)、和其他技术的整合做的例子，
也通过该例子使用maven做了一个archetype基础模板。可以通过该archetype来生成一个新的项目。

archetype基础模板使用说明:
1.点击根目录的install.bat进行初始化
2.点击archetype-generate.bat生成你的项目