<#assign shiro=JspTaglibs["/WEB-INF/taglib/shiro.tld"] />
<#assign core=JspTaglibs["/WEB-INF/taglib/jsp-jstl-core.tld"] />

<#macro buildNavigation children >
	<#if children?exists && children?has_content>
		<ul class="sub_navigation">
	    	<#list children as c>
	            <li>
	                <a href="${c.value}"><span class="${c.icon}">${c.name}</span></a>
	                <#if c.children?exists && c.children?has_content>
                    	<@buildNavigation children = c.children />
                    </#if>
	            </li>
	    	</#list>
        </ul>
	</#if>
</#macro>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>VCS 功能演示</title>


<!-- 用于布局的css -->
<link href="resources/styles/layout.css" rel="stylesheet" type="text/css" />
<!-- 图标的css -->
<link href="resources/styles/icon.css" rel="stylesheet" type="text/css" />
<!-- 控件布局的css -->
<link href="resources/styles/component.css" rel="stylesheet" type="text/css" />

<!-- jquery 核心代码 -->
<script type="text/javascript" src="resources/scripts/jquery.core.js"></script>
<script type="text/javascript" src="resources/scripts/jquery.form.js"></script>
<script type="text/javascript" src="resources/scripts/jquery.validate.js"></script>
<script type="text/javascript" src="resources/scripts/jquery.easydrag.js"></script>

<!-- vcs admin 所需要的代码 -->
<script type="text/javascript" src="resources/scripts/exit.jquery.extend.js"></script>
<script type="text/javascript" src="resources/scripts/exit.jquery.ui.js"></script>
<script type="text/javascript" src="resources/scripts/vcs.admin.config.js"></script>
</head>

<body>
	<!-- 主页整个布局开始 -->
    <div id="main_container">
	    <!-- logo开始-->
		<div id="logo">
			<!-- 用户登录后显示信息开始 -->
			<div id="account">
				<ul>
					<li>
						<a href="#"><span class="user24_icon"><@shiro.principal/></span></a>
					    <ul>
					        <li>
					            <a href="#"><span class="account24_icon">账户管理</span></a>
					        </li>
					        <li>
					            <a href="logout"><span class="logout24_icon">退出</span></a>
					        </li>
					    </ul>
					</li>
				</ul>
			</div>
			<!-- 用户登录后显示信息结束 -->
			<img src="resources/images/logo.png" />
		</div>
	    <!-- logo结束 -->
        
   		<!-- 菜单布局开始 -->
        <div id="menu_bar">
        	<!-- 导航按钮开始 -->
            <div id="navigation">
            	<ul>
                	<li>
                    	<a href="#"><span class="home32_icon">首 页</span></a>
                    </li>
                    <#list resourcesList as e>
	                    <li>
	                    	<a href="javascript:$.maskLoad({url:'${e.value}',urlCharReplaceEmpty:'/**',removeFirst:true,maskEl:'#main_content'})"><span class="${e.icon}">${e.name}</span></a>
	                    	<#if e.children?has_content>
	                    		<ul>
		                    		<#list e.children as c>
				                        <li>
				                            <a href="javascript:$.maskLoad({url:'${c.value}',urlCharReplaceEmpty:'/**',removeFirst:true,maskEl:'#main_content'})"><span class="${c.icon}">${c.name}</span></a>
				                        </li>
			                        </#list>
		                         </ul>
	                    	</#if>
	                    </li>
                    </#list>
                </ul>
                <!-- 导航按钮结束 -->
            </div>
    	</div>
        <!-- 菜单布局结束 -->
        <!-- 面包屑导航开始-->
        <div id="crumbs">
			首页
        </div>
        <!-- 面包屑导航结束-->
        <!-- 页面内容容器开始 -->
        <div id="main_content">
        	<@core.import url="account/user/view" />
    	</div>
        <!-- 页面内容容器结束 -->
        <div class="panel_footer" id="footer">
        	Copyright © 2011 VCS. Powered by vicnet .
        </div>
        
    </div>
    <!-- 主页整个布局结束 -->
</body>
</html>
