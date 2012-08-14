<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>VCS 用户登录</title>

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
	function login(){
		$("#login_form").submit();
	}
</script>

</head>

<body>

	<div id="login_container">
		<div class="logo"></div>
    	<div class="panel_title">
        	<span class="login24_icon">用户登录</span>
		</div>
        <div class="panel_content">
        	<form id="login_form" method="post" action="login">
            	<div class="column">
                	<label for="username">
                    	登录帐号:
                    </label>
                    <div class="field">
                    	<input type="text" name="username" id="username" class="text_input_big" size="25" value="admin"/>
                    </div>
                </div>
                <div class="column">
                	<label for="password">
                    	登录帐号:
                    </label>
                    <div class="field">
                    	<input type="password" name="password" id="password" class="text_input_big" size="25" value="admin"/>
                	</div>
                </div>
            </form>
        </div>
		<div class="panel_footer">
        	<a href="javascript:login();"><span class="button">登 录</span></a>
        </div>
    </div>
</body>