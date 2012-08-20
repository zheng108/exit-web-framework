exit-web-framework 
==================

exit-web-framework是对常用的java web开发封装实用功能来提高开发效率。exit-web-framework基于Spring 3、Hibernate4框架来对做项目核心三层和MVC的管理。使用到的新功能有spring缓存工厂、apeche shiro安全框架、spring mvc 3等主要技术，该项目分为两个部分做底层的封装，和带一个项目功能演示例子。

#### 初始化工作:

***

##### 配置maven

1. 下载[maven](http://maven.apache.org/download.html)
1. 解压缩maven-[版本]-bin.zip 到你想安装的位置
1. 设置maven系统环境变量，M2_HOME=[maven安装目录位置]
1. 添加maven bin目录至系统环境变量PATH中， %M2_HOME%\bin
1. 确认Maven的安装：cmd > mvn -version

##### 安装exit-web-framework到maven中

<p>
该安装过程会将exit-web-framework中的exit-common和exit-orm生成jar包放入mavee的.m2文件夹中
</p>
1. 使用git或者svn下载exit-web-framework
<pre>
git地址:git://github.com/exitsoft/exit-web-framework.git
svn地址:https://github.com/exitsoft/exit-web-framework.git
</pre>
1. 点击根目录下的bin/install.bat文件进行安装,当看见该信息时表示安装成功:
<pre>
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] exitsoft parend ................................... SUCCESS [2.331s]
[INFO] exitsoft common jar ............................... SUCCESS [6.575s]
[INFO] exitsoft orm jar .................................. SUCCESS [2.764s]
[INFO] vcs admin panel ................................... SUCCESS [8.346s]
[INFO] exit web framework project ........................ SUCCESS [0.390s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 22.171s
[INFO] Finished at: Mon Aug 20 16:24:34 CST 2012
[INFO] Final Memory: 29M/174M
[INFO] ------------------------------------------------------------------------
</pre>

##### exitsoft common jar

该jar包是对基本的常用工具类的一些简单封装。如泛型，反射，配置文件等工具类的封装。

##### exit-orm

该jar包是对持久化层的框架封装，目前只对Hibernate4的CURD和辅助功能封装。

##### 项目功能演示例子
<p>
在文件夹的shorcase里有一个vcs-admin项目。该项目是对以上两个框架(exit-common和exit-orm)、和其他技术的整合做的例子，也通过该例子使用maven做了一个archetype基础模板。可以通过该archetype来生成一个新的项目。
</p>
<p>
该项目使用了mysql数据库,在vcs-admin文件夹中有一个database的文件夹里面有对应该项目使用的数据。导入之后可以通过
vcs-admin项目文件夹中的bin文件夹下的jetty.bat文件运行项目，也可以用eclipse.bat生成项目导入到开发工具中在运行。
该工程下有一个基于jeety运行的java文件org.exitsoft.showcase.vcsadmin.app.LaunchJetty.你也可以通过该文件运行整个
项目.
</p>
###### archetype基础模板使用说明
1. 点击根目录的install.bat进行初始化
1. 点击archetype-generate.bat生成你的项目

##### 导入eclipse或者myeclipse
在根目录下的bin目录有一个eclipse.bat和myeclipse.bat，点击eclipse.bat/myeclipse.bat会生成project，看见一下信息表示生成成功，可以直接导入eclipse/myclipse
<pre>
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO]
[INFO] exitsoft parend ................................... SUCCESS [2.331s]
[INFO] exitsoft common jar ............................... SUCCESS [6.575s]
[INFO] exitsoft orm jar .................................. SUCCESS [2.764s]
[INFO] vcs admin panel ................................... SUCCESS [8.346s]
[INFO] exit web framework project ........................ SUCCESS [0.390s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 22.171s
[INFO] Finished at: Mon Aug 20 16:24:34 CST 2012
[INFO] Final Memory: 29M/174M
[INFO] ------------------------------------------------------------------------
</pre>