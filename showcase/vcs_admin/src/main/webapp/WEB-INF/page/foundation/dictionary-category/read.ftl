<div id="edit_form" class="form_widget">
	<form id="dictionary_category_form" action="foundation/dictionary-category/save" method="post">
		<input type="hidden" name="id" value="${entity.id!""}" />
	    <div class="column">
	        <label for="name">
				类别名称:
	        </label>
	        <div class="field">
	            <input type="text" id="name" name="name" class="text_input_big required" size="25" value="${entity.name!""}"/>
	        </div>
	        <label for="code">
				类别代码:
	        </label>
	        <div class="field">
	           <input type="text" id="code" name="code" class="text_input_big required" size="25" value="${entity.code!""}"/>
	         </div>
	    </div>
	    <div class="column">
	    	<label for="parentId">
	    		所属父类:
	    	</label>
	    	<div class="field">
	    		<select class="selection" height="150" search="true" name="parentId" id="parentId" size="66">
	                <#list categoriesList as cl>
	                	<option value="${cl.id}" <#if entity.parentId==cl.id> selected="selected" </#if> >${cl.name}</option>
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
		<a href="javascript:$.form.resetForm('#dictionary_category_form')" title="清空表单"><span class="button right">重 置</span></a>
		<a href="javascript:$.form.submitMaskForm('#dictionary_category_form',{maskEl:'#edit_dictionary_category',target:'#main_content'});" title="保存信息"><span class="button right">保 存</span></a>
	</div>
</div>