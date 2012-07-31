<div id="update_user" class="form_widget">
	<form id="user_form" action="account/user/update" method="post">
		<input type="hidden" name="id" value="${entity.id!""}" />
	    <div class="column">
	        <label for="username">
				登录帐号:
	        </label>
	        <div class="field">
	            <input type="text" readonly="readonly" id="username" name="username" class="text_input_big" size="25" value="${entity.username!""}"/>
	        </div>
	        <label for="realname">
				真实姓名:
	        </label>
	        <div class="field">
	            <input type="text" id="realname" name="realname" class="text_input_big" size="25" value="${entity.realname!""}"/>
	        </div>
	    </div>
	    <div class="column">
	        <label for="email">
				电子邮件:
	        </label>
	        <div class="field">
	          <input type="text" id="email" name="email" class="text_input_big" size="25" value="${entity.email!""}" />
	        </div>
	        <label for="state">
				状态:
	        </label>
	        <div class="field">
	           <select class="selection" name="state" id="state" size="25">
	           		<#list states as s>
		                <option value="${s.value}" <#if entity.state == s.value?number >selected="selected"</#if>>
							${s.name}
		                </option>
	                </#list>
	           </select>
	         </div>
	    </div>
	    <div class="column">
	    	<label for="groupIds">
	    		所在组:
	    	</label>
	    	<div class="field">
	    		<select class="selection" search="true" multiple="multiple" name="groupIds" id="groupIds" size="66">
	                <#list groupsList as gl>
	                	<#assign isSelected = false />
	                	<#list entity.groupsList as egl>
	                		<#if egl.id == gl.id>
	                			<#assign isSelected = true />
	                			<#break />
	                		</#if>
	                	</#list>
	                	<option value="${gl.id}" <#if isSelected> selected="selected" </#if> >${gl.name}</option>
	                </#list>
	           </select>
	    	</div>
	    </div>
	</form>
	<div class="clear">
		<a href="javascript:$.form.resetForm('#user_form')" title="清空表单"><span class="button right">重 置</span></a>
		<a href="javascript:$.form.submitMaskForm('#user_form',{maskEl:'#update_user',target:'#main_content'});" title="保存信息"><span class="button right">保 存</span></a>
	</div>
</div>