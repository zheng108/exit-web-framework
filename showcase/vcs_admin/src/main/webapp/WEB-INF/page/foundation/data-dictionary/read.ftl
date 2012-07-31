<div id="edit_form" class="form_widget">
	<form id="data_dictionary_form" action="foundation/data-dictionary/save" method="post">
		<input type="hidden" name="id" value="${entity.id!""}" />
	    <div class="column">
	        <label for="name">
				字典名称:
	        </label>
	        <div class="field">
	            <input type="text" id="name" name="name" class="text_input_big required" size="25" value="${entity.name!""}"/>
	        </div>
	        <label for="value">
				字典值:
	        </label>
	        <div class="field">
	           <input type="text" id="value" name="value" class="text_input_big required" size="25" value="${entity.value!""}"/>
	         </div>
	    </div>
	    <div class="column">
	    	<label for="type">
				值类型:
	        </label>
	        <div class="field">
	           <select class="selection" name="type" id="type" size="25">
	                <#list valueTypes as vt>
	                	<option value="${vt.value}" <#if entity.type==vt.value> selected="selected" </#if> >${vt.name}</option>
	                </#list>
	           </select>
	        </div>
	    	<label for="categoryId">
	    		所属类型:
	    	</label>
	    	<div class="field">
	    	   <select class="selection" height="150" search="true" name="categoryId" id="categoryId" size="25">
	                <#list categoriesList as cl>
	                	<option value="${cl.id}" <#if entity.category?has_content && entity.category.id==cl.id> selected="selected" </#if> >${cl.name}</option>
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
		<a href="javascript:$.form.resetForm('#data_dictionary_form')" title="清空表单"><span class="button right">重 置</span></a>
		<a href="javascript:$.form.submitMaskForm('#data_dictionary_form',{maskEl:'#edit_data_dictionary',target:'#main_content'});" title="保存信息"><span class="button right">保 存</span></a>
	</div>
</div>