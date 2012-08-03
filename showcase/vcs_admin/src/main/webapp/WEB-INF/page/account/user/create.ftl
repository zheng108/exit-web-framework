<div id="create_form" class="form_widget">
	<form id="user_form" action="account/user/insert" method="post">
	    <div class="column">
	        <label for="username">
				登录帐号:
	        </label>
	        <div class="field">
	            <input type="text" id="username" name="username" placeholder="用户登录的用户名" class="text_input_big required" remote="account/user/isUsernameUnique" size="25" />
	        </div>
	        <label for="realname">
				真实姓名:
	        </label>
	        <div class="field">
	            <input type="text" id="realname" name="realname" class="text_input_big" size="25" />
	        </div>
	    </div>
	    <div class="column">
	        <label for="password">
				登录密码:
	        </label>
	        <div class="field">
	            <input type="password" id="password" placeholder="用户登录的密码" name="password" class="text_input_big required" size="25" />
	        </div>
	        <label for="configPassword">
				确认密码:
	        </label>
	        <div class="field">
	            <input type="password" id="configPassword" placeholder="确认登录密码" name="configPassword" class="text_input_big required" equalTo="#password" size="25" />
	        </div>
	    </div>
	    <div class="column">
	        <label for="email">
				电子邮件:
	        </label>
	        <div class="field">
	          <input type="text" id="email" name="email" class="text_input_big" size="25" />
	        </div>
	        <label for="state">
				状态:
	        </label>
	        <div class="field">
	           <select class="selection" name="state" id="state" size="25">
	               <#list states as s>
		                <option value="${s.value}">
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
	                	<option value="${gl.id}">
	                		${gl.name}
	                	</option>
	                </#list>
	           </select>
	    	</div>
	    </div>
	</form>
	<div class="clear">
		<a href="javascript:$.form.resetForm('#user_form')" title="清空表单"><span class="button right">重 置</span></a>
		<a href="javascript:$.form.submitMaskForm('#user_form',{maskEl:'#create_user',target:'#main_content',resetForm:true});" title="保存信息"><span class="button right">保 存</span></a>
	</div>
</div>