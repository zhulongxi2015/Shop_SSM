package cn.itcast.ssm.controller.interceptor;

import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.po.SysPermission;
import cn.itcast.ssm.util.ResourcesUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 授权拦截器
 */
public class AuthorazitionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        //得到请求的url
        String url = request.getRequestURI();

        //判断是否是公开 地址
        //实际开发中需要公开 地址配置在配置文件中
        //从配置中取逆名访问url

        List<String> open_urls = ResourcesUtil.gekeyList("anonymousURL");
        //遍历公开 地址，如果是公开 地址则放行
        for(String open_url:open_urls){
            if(url.indexOf(open_url)>=0){
                //如果是公开 地址则放行
                return true;
            }
        }

        //从配置文件中获取公共访问地址
        List<String> common_urls = ResourcesUtil.gekeyList("commonURL");
        //遍历公用 地址，如果是公用 地址则放行
        for(String common_url:common_urls){
            if(url.indexOf(common_url)>=0){
                //如果是公开 地址则放行
                return true;
            }
        }

        //获取session
        HttpSession session = request.getSession();
        ActiveUser activeUser = (ActiveUser) session.getAttribute("activeUser");
        //从session中取权限范围的url
        List<SysPermission> permissions = activeUser.getPermissions();
        for(SysPermission sysPermission:permissions){
            //权限的url
            String permission_url = sysPermission.getUrl();
            if(url.indexOf(permission_url)>=0){
                //如果是权限的url 地址则放行
                return true;
            }
        }
        //执行到这里拦截，跳转到无权访问的提示页面
        request.getRequestDispatcher("/WEB-INF/jsp/refuse.jsp").forward(request, response);

        //如果返回false表示拦截不继续执行handler，如果返回true表示放行
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
