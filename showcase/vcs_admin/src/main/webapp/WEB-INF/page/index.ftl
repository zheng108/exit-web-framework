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
		<script type="text/javascript" src="resources/scripts/jquery.poshytip.js"></script>
		<script type="text/javascript" src="resources/scripts/jquery.placeholder.js"></script>
		
		<!-- vcs admin 所需要的代码 -->
		<script type="text/javascript" src="resources/scripts/exit.jquery.extend.js"></script>
		<script type="text/javascript" src="resources/scripts/exit.jquery.ui.js"></script>
		<script type="text/javascript" src="resources/scripts/vcs.admin.config.js"></script>
		
		<script type="text/javascript">
		
			/**
			*
			* 修改密码提交表单
			*
			**/
			function changePassword(){
				var form = $("#change_password_form");
				
				form.ajaxForm({
					success:function(responseText){
						if(responseText == "true") {
							alert("修改密码成功");
							form.clearForm();
							$("#change_password_dailog").hide();
						} else {
							alert("旧密码不正确");
						}
					}
				});
				
				form.submit();
			}
			
			/**
			*
			* 设置固定宽度
			*
			**/
			function fixed() {
				$("#main_container").animate({width:1024},500);
			}
			
			/**
			*
			* 设置满屏宽度
			*
			**/
			function full(){
				$("#main_container").animate({
					width:$("body").width() - 20
				},500);
			}
			
			/**
			*
			* 加载菜单连接,联动到面包屑
			*
			**/
			function loadModule(o,url){
			
				var me = $(o),crumbs = $("#crumbs");
				var lis = me.parents("li");
				
				crumbs.empty();
				
				for (var i =  lis.length - 1; i >= 0; i--) {
				
					var o = $(lis[i]);
					crumbs.append(o.children().children().first().text());
					
					if (i > 0) {
						crumbs.append(" -> ");
					}
					
				}
				
				$.maskLoad({url:url,urlCharReplaceEmpty:'/**',removeFirst:true,target:'#main_content'})
			}
			
		</script>
	</head>
	
	<body>
		<div id="change_password_panel">
			<form id="change_password_form" method="post" action="changePassword">
				<div class="column">
			        <label for="oldPassword">
						旧密码:
			        </label>
			        <div class="field">
			            <input type="password" id="oldPassword" name="oldPassword" class="text_input_big" size="25" />
			        </div>
			    </div>
			    <div class="column">
			        <label for="newPassword">
						新密码:
			        </label>
			        <div class="field">
			            <input type="password" id="newPassword" name="newPassword" class="text_input_big" size="25" />
			        </div>
			     </div>
			     <div class="column">
			        <label for="confirmPassword">
						确认密码:
			        </label>
			        <div class="field">
			            <input type="password" id="confirmPassword" name="confirmPassword" class="text_input_big" size="25" />
			        </div>
			    </div>
			</form>
			<div class="clear">
				<a href="javascript:$.form.resetForm('#change_password_form');" title="重置表单"><span class="button right">重 置</span></a>
				<a href="javascript:changePassword();" title="确认修改密码"><span class="button right">确 认</span></a>
			</div>
		</div>
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
						    	<@shiro.hasPermission name="security:change-password">
							        <li>
							            <a href="#change_password_panel" width="320" title="修改密码" icon="group24_icon" target="dialog" dialogId="change_password_dailog" ><span class="account24_icon">修改密码</span></a>
							        </li>
							    </@shiro.hasPermission>
						        <li>
						            <a href="javascript:fixed();"><span class="fixed24_icon">固定宽度</span></a>
						        </li>
						        <li>
						            <a href="javascript:full();"><span class="full24_icon">满屏宽度</span></a>
						        </li>
						        <li>
						            <a href="logout"><span class="logout24_icon">退出</span></a>
						        </li>
						    </ul>
						</li>
					</ul>
				</div>
				<!-- 用户登录后显示信息结束 -->
				<div class="logo"></div>
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
	                    <#list menusList as e>
		                    <li>
		                    	<a><span class="${e.icon}">${e.name}</span></a>
		                    	<#if e.children?has_content>
		                    		<ul>
			                    		<#list e.children as c>
					                        <li>
					                           <a href="javascript:void(0);"><span onclick="loadModule(this,'${c.value}');" class="${c.icon}">${c.name}</span></a>
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
				权限管理 -> 用户管理 
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
