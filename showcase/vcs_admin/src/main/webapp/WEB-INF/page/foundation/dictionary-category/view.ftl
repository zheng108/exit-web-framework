<#assign shiro=JspTaglibs["/WEB-INF/taglib/shiro.tld"] />
<script>
	$(document).ready(function(){
		$(".close").click(
			function () {
				$(this).parent().fadeTo(400, 0, function () {
					$(this).slideUp(400);
				});
				return false;
			}
		);
		$("#selectAll").click(function(){
			var checks = $("#dictionary_category_list tbody").find("input[type='checkbox'][id!='selectAll']");
			checks.attr('checked', $(this).is(':checked'));
			checks.click();
		});
	});
	
	function search(pageNo){
		var param = $.form.getParameters("#search_form",true);
		param["pageSize"] = $("#pageSize").val();
		param["pageNo"] = pageNo || 1;
		$.maskLoad({
			url:'foundation/dictionary-category/view',
			param:param,
			target:'#main_content'
		});
	}
</script>
<div id="dictionary_category_panel">
	<div id="search_dictionary_category_dailog">
		<form id="search_form" method="post" action="foundation/dictionary-category/view">
			<div class="column">
		        <label for="filter_LIKE_S_name">
					类别名称:
		        </label>
		        <div class="field">
		            <input type="text" id="filter_LIKE_S_name" name="filter_LIKE_S_name" class="text_input_big" size="25" value="${RequestParameters.filter_LIKE_S_name!""}"/>
		        </div>
		        <label for="filter_EQ_S_code">
					类别代码:
		        </label>
		        <div class="field">
		            <input type="text" id="filter_EQ_S_code" name="filter_EQ_S_code" class="text_input_big" size="25" value="${RequestParameters.filter_EQ_S_code!""}" />
		        </div>
		    </div>
		    <div class="column">
		    	<label for="filter_EQ_S_parent.id">
		    		所属父类:
		    	</label>
		    	<div class="field">
		    		<select class="selection" height="150" search="true" name="filter_EQ_S_parent.id" id="filter_EQ_S_parent.id" size="66">
		    			<option>无</option>
		                <#list categoriesList as cl>
		                	<option value="${cl.id}" <#if RequestParameters['filter_EQ_S_parent.id']?has_content && RequestParameters['filter_EQ_S_parent.id'] == cl.id>selected="selected"</#if> >${cl.name}</option>
		                </#list>
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
	 	<span class="dictionary_category24_icon">字典类别管理</span>
	</div>
	
	<div class="panel_content">
	  <#if message?has_content>
	  	  <div class="notification information">
	  	  	<a href="#" class="close"><img src="resources/images/icons/16/close.png" title="关闭信息" alt="关闭"></a>
	  	  	${message}
	  	  </div>
  	  </#if>
	  <form id="delete_form" action="foundation/dictionary-category/delete" method="post">
		  <table width="100%" id="dictionary_category_list">
		    	<thead>
		        	<tr>
		        		<th>
		        			<div style="width:15px;height:15px;margin:0 auto;">
		        				<input type="checkbox" id="selectAll" class="checker"/>
		        			</div>
		        		</th>
		            	<th>
		                	类别名称
		            	</th>
		                <th>
		                	类别代码
		            	</th>
		                <th>
		                	所属父类
		            	</th>
		                <th>
		                	备注
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
		            			<div style="width:15px;height:15px;margin:0 auto;">
		            				<input type="checkbox" name="ids" value="${e.id}" class="checker"/>
		            			</div>
		            		</td>
		                	<td>
		                    	${e.name!""}
		                    </td>
		                    <td>
		                    	${e.code!""}
		                    </td>
		                    <td>
		                    	${e.parentName}
		                    </td>
		                    <td>
		                    	${e.remark!""}
		                    </td>
		                    <td align="center">
		                    	<@shiro.hasPermission name="dictionary-category:save">
		                    		<a href="foundation/dictionary-category/read?id=${e.id}" icon="dictionary_category24_icon"  width="610" target="dialog" dialogId="edit_dictionary_category" modal="true" title="修改字典类别/${e.name}" class="operat edit16_icon">
		                    	</@shiro.hasPermission>
		                    </td>
		                </tr>
		            </#list>
		        </tbody>
		    </table>
		</form>
	</div>
	
	<div class="panel_footer">
		<@shiro.hasPermission name="dictionary-category:save">
			<a href="foundation/dictionary-category/read" icon="dictionary_category24_icon" width="610" target="dialog" dialogId="edit_dictionary_category" modal="true" title="添加字典类别">
				<span class="button left">添 加</span>
			</a>
		</@shiro.hasPermission>
		<@shiro.hasPermission name="dictionary-category:delete">
	    	<a href="javascript:$.form.submitMaskForm('#delete_form',{maskEl:'#dictionary_category_panel',target:'#main_content',promptMsg:'确定要删除吗?'})" title="删除选中字典类别">
	    		<span class="button left">删 除</span>
	    	</a>
	    </@shiro.hasPermission>
	    <a href="#search_dictionary_category_dailog" width="610" icon="dictionary_category24_icon" target="dialog" dialogId="search_dailog" title="查询字典类别列表">
	    	<span class="button left">查 询</span>
	    </a>
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
			        <a href="javascript:search(${page.prePage})">
			        	上一页
			        </a>
		        </#if>
		        <#list page.getSlider(page.pageSize) as index>
			        <a class="number <#if page.pageNo == index>current</#if>" href="javascript:search(${index})">
			        	${index}
			        </a>
		        </#list>
		        <#if page.hasNextPage()>
			        <a href="javascript:search(${page.nextPage})">
			        	下一页
			        </a>
			        <a href="javascript:search(${page.totalPages})">
			        	尾页
			        </a>
		        </#if>
		    </div>
		</div>
	</#if>
</div>