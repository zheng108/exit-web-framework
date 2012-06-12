package org.exitsoft.project.vcsadmin.service.account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

@Component
public class RememberMeDateManager extends CookieRememberMeManager{

	@Override
	public void onSuccessfulLogin(Subject subject, AuthenticationToken token,AuthenticationInfo info) {

        forgetIdentity(subject);
        HttpServletRequest request = WebUtils.getHttpRequest(subject);
        String value = request.getParameter(DEFAULT_REMEMBER_ME_COOKIE_NAME);
        
        if (StringUtils.isNotEmpty(value) && token instanceof UsernamePasswordToken) {
        	((UsernamePasswordToken)token).setRememberMe(true);
        }
        
        super.onSuccessfulLogin(subject, token, info);
	}
	
	@Override
	protected void rememberSerializedIdentity(Subject subject, byte[] serialized) {
		if (!WebUtils.isHttp(subject)) {
            return;
        }

        HttpServletRequest request = WebUtils.getHttpRequest(subject);
        HttpServletResponse response = WebUtils.getHttpResponse(subject);

        String value = request.getParameter(DEFAULT_REMEMBER_ME_COOKIE_NAME);
        
        String base64 = Base64.encodeToString(serialized);

        Cookie template = getCookie(); 
        Cookie cookie = new SimpleCookie(template);
        cookie.setValue(base64);
        cookie.setMaxAge(RememberMeType.valueOf(value).getValue());
        cookie.saveTo(request, response);
	}
}
