package cn.itcast.ssm.shiro;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 自定义表单身份认证，实现在认证前实现验证码校验
 */
public class CustomAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //进行验证码校验
        //校验验证码
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        String validCode=(String) httpServletRequest.getSession().getAttribute("validateCode");
        String randomCode=httpServletRequest.getParameter("randomcode");
        if(validCode!=null && randomCode!=null && !randomCode.equals(validCode)){
            httpServletRequest.setAttribute("shiroLoginFailure","randomCodeError");
            return true;
        }
        return super.onAccessDenied(request, response);
    }
}
