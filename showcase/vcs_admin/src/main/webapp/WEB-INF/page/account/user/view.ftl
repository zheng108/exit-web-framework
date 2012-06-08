<#assign shiro=JspTaglibs["/WEB-INF/taglib/shiro.tld"] />
<script>
	function search(pageNo){
		var param = $.form.getParameters("#search_form",true);
		param["pageSize"] = $("#pageSize").val();
		param["pageNo"] = pageNo || 1;
		$.maskLoad({
			url:'account/user/view',
			param:param,
			target:'#main_content'
		});
	}
</script>
<div id="user_panel">
	<div id="search_user_dailog">
		<form id="search_form" method="post" action="account/user/view">
			<div class="column">
		        <label for="username">
					登录帐号:
		        </label>
		        <div class="field">
		            <input type="text" id="username" name="filter_RLIKE_S_username" class="text_input_big" size="25" value="${request.filter_RLIKE_S_username!""}"/>
		        </div>
		        <label>
					真实姓名:
		        </label>
		        <div class="field">
		            <input type="text" id="realname" name="filter_RLIKE_S_realname" class="text_input_big" size="25" />
		        </div>
		    </div>
		    <div class="column">
		        <label for="email">
					电子邮件:
		        </label>
		        <div class="field">
		          <input type="text" id="email" name="filter_RLIKE_S_email" class="text_input_big" size="25" />
		        </div>
		        <label for="state">
					状态:
		        </label>
		        <div class="field">
		           <select class="selection" name="filter_EQ_S_state" id="state" size="25">
		           		<option>
							全部
		                </option>
		                <option value="0">
							启用
		                </option>
		                <option value="1">
							禁用
		                </option>
		           </select>
		         </div>
		    </div>
		</form>
		<div class="clear">
			<a href="javascript:$.resetForm('#search_form')" title="清空表单"><span class="button right">重 置</span></a>
			<a href="javascript:search();" title="查询输入信息"><span class="button right">查 询</span></a>
		</div>
	</div>
	
	<div class="panel_title">
	 	<span class="user24_icon">用户管理</span>
	</div>
	
	<div class="panel_content">
  	  
	  <form id="delete_form" action="account/user/delete" method="post">
		  <table width="100%">
		    	<thead>
		        	<tr>
		        		<th>
		        			<input type="checkbox" class="selectAll" />
		        		</th>
		            	<th>
		                	登录帐号
		            	</th>
		                <th>
		                	真实姓名
		            	</th>
		                <th>
		                	电子邮件
		            	</th>
		                <th>
		                	状态
		            	</th>
		                <th>
		                	所在组
		            	</th>
		            	<th>
		            		操作
		            	</th>
		            </tr>
		        </thead>
		        <tbody>
		        	<#list page.result as e>
		            	<tr>
		            		<td>
		            			<input type="checkbox" name="ids" value="${e.id}"/>
		            		</td>
		                	<td>
		                    	${e.username}
		                    </td>
		                    <td>
		                    	${e.realname}
		                    </td>
		                    <td>
		                    	${e.email}
		                    </td>
		                    <td>
		                    	${e.stateName}
		                    </td>
		                    <td>
		                    	${e.groupNames}
		                    </td>
		                    <td class="operat">
		                    	<@shiro.hasPermission name="user:update">
		                    	<a href="account/user/read?id=${e.id}" icon="user24_icon"  width="610" target="dialog" dialogId="update_user" modal="true" title="修改用户/${e.username}" class="edit16_icon">
		                    	</@shiro.hasPermission>
		                    </td>
		                </tr>
		            </#list>
		        </tbody>
		    </table>
		</form>
	</div>
	<div class="panel_footer">
		<@shiro.hasPermission name="user:create">
			<a href="account/user/read" icon="user24_icon" width="610" target="dialog" dialogId="create_user" modal="true" title="添加用户"><span class="button left">添 加</span></a>
		</@shiro.hasPermission>
		<@shiro.hasPermission name="user:delete">
	    	<a href="javascript:$.form.submitMaskForm('#delete_form',{maskEl:'#user_panel',target:'#main_content',promptMsg:'确定要删除吗?'})" title="删除选中用户"><span class="button left">删 除</span></a>
	    </@shiro.hasPermission>
	    <a href="#search_user_dailog" width="610" icon="user24_icon" target="dialog" dialogId="search_dailog" title="查询用户列表"><span class="button left">查 询</span></a>
	</div>
	
	<#if page.totalItems gt 0>
		<div class="panel_pagging">
		 	<div class="page_bar">
		    	每页显示:<input type="text" size="2" id="pageSize" name="pageSize" class="text_input_small" value="${page.pageSize}"/> 共有${page.totalPages}页/${page.totalItems}条记录
		    </div>
		    <div class="page_bar">
		    	<#if page.hasPrePage()>
			    	<a href="javascript:search(1)">
			        	首页
			        </a>
			        <a href="javascript:search(${page.prePage()})">
			        	上一页
			        </a>
		        </#if>
		        <#list page.getSlider(page.pageSize) as index>
			        <a class="number <#if page.pageNo == index>current</#if>" href="javascript:search(${index})">
			        	${index}
			        </a>
		        </#list>
		        <#if page.hasNextPage()>
			        <a href="javascript:search(${page.nextPage()})">
			        	下一页
			        </a>
			        <a href="javascript:search(${page.totalPages()})">
			        	尾页
			        </a>
		        </#if>
		    </div>
		</div>
	</#if>
</div>