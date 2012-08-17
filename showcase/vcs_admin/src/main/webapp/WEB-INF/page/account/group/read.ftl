<div id="edit_form" class="form_widget">
	<form id="group_form" action="account/group/save" method="post">
		<input type="hidden" name="id" value="${entity.id!""}" />
		<input type="hidden" name="type" value="03" />
	    <div class="column">
	        <label for="name">
				组名称:
	        </label>
	        <div class="field">
	            <input type="text" id="name" name="name" class="text_input_big required" size="25" value="${entity.name!""}"/>
	        </div>
	        <label for="state">
				状态:
	        </label>
	        <div class="field">
	           <select class="selection" name="state" id="state" size="25">
	           		<#list states as s>
		           		<option value="${s.value}" <#if entity.state == s.value?number >selected="selected"</#if> >
							${s.name}
		                </option>
	           		</#list>
	           </select>
	         </div>
	    </div>
	    <div class="column">
	    	<label for="parentId">
	    		所属父类:
	    	</label>
	    	<div class="field">
	    		<select class="selection" height="150" search="true" name="parentId" id="parentId" size="66">
	                <#list groupsList as gl>
	                	<option value="${gl.id}" <#if entity.parentId==gl.id> selected="selected" </#if> >${gl.name}</option>
	                </#list>
	           </select>
	    	</div>
	    </div>
	    <div class="column">
	    	<label for="resourceIds">
	    		拥有资源:
	    	</label>
	    	<div class="field">
	    		<select class="selection" height="150" multiple="multiple" search="true" name="resourceIds" id="resourceIds" size="66">
	                <#list resourcesList as rl>
	                	<#assign isSelected = false />
	                	<#list entity.resourcesList as erl>
	                		<#if erl.id == rl.id>
	                			<#assign isSelected = true />
	                			<#break />
	                		</#if>
	                	</#list>
	                	<option value="${rl.id}" <#if isSelected> selected="selected" </#if> >${rl.name}</option>
	                </#list>
	           </select>
	    	</div>
	    </div>
	    <div class="column">
	    	<label for="remark">
	    		备注:
	    	</label>
	    	<div class="field">
	    		<input type="text" id="icon" name="remark" class="text_input_big" size="66" value="${entity.remark!""}" />
	    	</div>
	    </div>
	</form>
	<div class="clear">
		<a href="javascript:$.form.resetForm('#group_form')" title="清空表单"><span class="button right">重 置</span></a>
		<a href="javascript:$.form.submitMaskForm('#group_form',{maskEl:'#edit_group',target:'#main_content'});" title="保存信息"><span class="button right">保 存</span></a>
	</div>
</div>