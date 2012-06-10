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
			var checks = $("#data_dictionary_list tbody").find("input[type='checkbox'][id!='selectAll']");
			checks.attr('checked', $(this).is(':checked'));
			checks.click();
		});
	});
	
	function search(pageNo){
		var param = $.form.getParameters("#search_form",true);
		param["pageSize"] = $("#pageSize").val();
		param["pageNo"] = pageNo || 1;
		$.maskLoad({
			url:'foundation/data-dictionary/view',
			param:param,
			target:'#main_content'
		});
	}
</script>
<div id="data_dictionary_panel">
	<div id="search_data_dictionary_dailog">
		<form id="search_form" method="post" action="foundation/data-dictionary/view">
			<div class="column">
		        <label for="filter_LIKE_S_name">
					字典名称:
		        </label>
		        <div class="field">
		            <input type="text" id="filter_LIKE_S_name" name="filter_LIKE_S_name" class="text_input_big" size="25" value="${RequestParameters.filter_LIKE_S_name!""}"/>
		        </div>
		        <label for="filter_EQ_S_code">
					字典代码:
		        </label>
		        <div class="field">
		            <input type="text" id="filter_EQ_S_code" name="filter_EQ_S_code" class="text_input_big" size="25" value="${RequestParameters.filter_EQ_S_code!""}" />
		        </div>
		    </div>
		    <div class="column">
		    	<label for="filter_EQ_S_type">
		    		值类型:
		    	</label>
		    	<div class="field">
		    		<select class="selection" name="filter_EQ_S_type" id="filter_EQ_S_type" size="25">
		    			<option>无</option>
		                <#list valueTypes as vt>
		                	<option value="${vt.value}" <#if RequestParameters.filter_EQ_S_type?has_content && RequestParameters.filter_EQ_S_type == vt.value>selected="selected"</#if> >${vt.name}</option>
		                </#list>
		            </select>
		    	</div>
		    	<label for="filter_EQ_S_parent.id">
		    		所属父类:
		    	</label>
		    	<div class="field">
		    		<select class="selection" height="150" search="true" name="filter_EQ_S_parent.id" id="filter_EQ_S_parent.id" size="25">
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
	 	<span class="data_dictionary24_icon">字典字典管理</span>
	</div>
	
	<div class="panel_content">
	  <#if message?has_content>
	  	  <div class="notification information">
	  	  	<a href="#" class="close"><img src="resources/images/icons/16/close.png" title="关闭信息" alt="关闭"></a>
	  	  	${message}
	  	  </div>
  	  </#if>
	  <form id="delete_form" action="foundation/data-dictionary/delete" method="post">
		  <table width="100%" id="data_dictionary_list">
		    	<thead>
		        	<tr>
		        		<th>
		        			<div style="width:15px;height:15px;margin:0 auto;">
		        				<input type="checkbox" id="selectAll" class="checker"/>
		        			</div>
		        		</th>
		            	<th>
		                	字典名称
		            	</th>
		                <th>
		                	字典值
		            	</th>
		            	<th>
		                	值类型
		            	</th>
		                <th>
		                	所属类别
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
		                    	${e.value!""}
		                    </td>
		                    <td>
		                    	${e.type!""}
		                    </td>
		                    <td>
		                    	<#if e.category?has_content>${e.category.name}</#if>
		                    </td>
		                    <td>
		                    	${e.remark!""}
		                    </td>
		                    <td align="center">
		                    	<@shiro.hasPermission name="data-dictionary:save">
		                    		<a href="foundation/data-dictionary/read?id=${e.id}" icon="data_dictionary24_icon"  width="610" target="dialog" dialogId="edit_data_dictionary" modal="true" title="修改字典字典/${e.name}" class="operat edit16_icon">
		                    	</@shiro.hasPermission>
		                    </td>
		                </tr>
		            </#list>
		        </tbody>
		    </table>
		</form>
	</div>
	
	<div class="panel_footer">
		<@shiro.hasPermission name="data-dictionary:save">
			<a href="foundation/data-dictionary/read" icon="data_dictionary24_icon" width="610" target="dialog" dialogId="edit_data_dictionary" modal="true" title="添加字典字典">
				<span class="button left">添 加</span>
			</a>
		</@shiro.hasPermission>
		<@shiro.hasPermission name="data-dictionary:delete">
	    	<a href="javascript:$.form.submitMaskForm('#delete_form',{maskEl:'#data_dictionary_panel',target:'#main_content',promptMsg:'确定要删除吗?'})" title="删除选中字典字典">
	    		<span class="button left">删 除</span>
	    	</a>
	    </@shiro.hasPermission>
	    <a href="#search_data_dictionary_dailog" width="610" icon="data_dictionary24_icon" target="dialog" dialogId="search_dailog" title="查询字典字典列表">
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