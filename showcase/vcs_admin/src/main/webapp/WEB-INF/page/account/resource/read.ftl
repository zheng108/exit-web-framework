<div id="edit_form" class="form_widget">
	<form id="resourc_form" action="account/resource/save" method="post">
		<input type="hidden" name="id" value="${entity.id!""}" />
	    <div class="column">
	        <label for="name">
				资源名称:
	        </label>
	        <div class="field">
	            <input type="text" id="name" name="name" class="text_input_big" size="25" value="${entity.name!""}"/>
	        </div>
	        <label for="type">
				资源类型:
	        </label>
	        <div class="field">
	           <select class="selection" name="type" id="type" size="25">
	           		<#list resourceType as rt>
		           		<option value="${rt.value}" <#if entity.type?has_content && entity.type == rt.value >selected="selected"</#if> >
							${rt.name}
		                </option>
	           		</#list>
	           </select>
	         </div>
	    </div>
	    <div class="column">
	        <label for="value">
				拦截URL:
	        </label>
	        <div class="field">
	          <input type="text" id="value" name="value" class="text_input_big" size="25" value="${entity.value!""}" />
	        </div>
	        <label for="permission">
				角色访问:
	        </label>
	        <div class="field">
	          <input type="text" id="permission" name="permission" class="text_input_big" size="25" value="${entity.permission!""}" />
	        </div>
	    </div>
	    <div class="column">
	        <label for="sort">
				顺序值:
	        </label>
	        <div class="field">
	          <input type="text" id="sort" name="sort" class="text_input_big" size="25" value="${entity.sort!""}" />
	        </div>
	        <label for="icon">
				图标:
	        </label>
	        <div class="field">
	          <input type="text" id="icon" name="icon" class="text_input_big" size="25" value="${entity.icon!""}" />
	        </div>
	    </div>
	    <div class="column">
	    	<label for="parendId">
	    		所属父类:
	    	</label>
	    	<div class="field">
	    		<select class="selection" height="150" search="true" name="parendId" id="parendId" size="66">
	                <#list resourcesList as rl>
	                	<option value="${rl.id}" <#if entity.parentId==rl.id> selected="selected" </#if> >${rl.name}</option>
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
		<a href="javascript:$.form.resetForm('#resourc_form')" title="清空表单"><span class="button right">重 置</span></a>
		<a href="javascript:$.form.submitMaskForm('#resourc_form',{maskEl:'#edit_resource',target:'#main_content'});" title="保存信息"><span class="button right">保 存</span></a>
	</div>
</div>