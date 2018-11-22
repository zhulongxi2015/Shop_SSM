package cn.itcast.ssm.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.itcast.ssm.exception.CustomException;
import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.service.SysService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@Autowired
	private SysService sysService;
	//用户登陆提交方法
	/*@RequestMapping("/login")
	public String login(HttpSession session,String randomcode, String usercode,String password)throws Exception{

		//校验验证码
		String validCode=(String) session.getAttribute("validateCode");
		if(!randomcode.equals(validCode)){
			throw new CustomException("验证码输入错误");
		}
		//调用service校验用户账号和密码的正确性
		ActiveUser activeUser= sysService.authenticat(usercode,password);
		
		//如果service校验通过，将用户身份记录到session
		session.setAttribute("activeUser", activeUser);
		//重定向到商品查询页面
		return "redirect:/first.action";
	}
	*/
	//登陆提交地址，和applicationContext-shiro.xml中配置的loginurl一致
	@RequestMapping("login")
	public String login(HttpServletRequest request)throws Exception{

		//如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		//根据shiro返回的异常类路径判断，抛出指定异常信息
		if(exceptionClassName!=null){
			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				//最终会抛给异常处理器
				throw new CustomException("账号不存在");
			} else if (IncorrectCredentialsException.class.getName().equals(
					exceptionClassName)) {
				throw new CustomException("用户名/密码错误");
			} else if("randomCodeError".equals(exceptionClassName)){
				throw new CustomException("验证码错误 ");
			}else {
				throw new Exception();//最终在异常处理器生成未知错误
			}
		}
		//此方法不处理登陆成功（认证成功），shiro认证成功会自动跳转到上一个请求路径
		//登陆失败还到login页面
		return "login";
	}
	//用户退出
	/*@RequestMapping("/logout")
	public String logout(HttpSession session)throws Exception{

		//session失效
		session.invalidate();
		//重定向到商品查询页面
		return "redirect:/first.action";

	}*/
	

}
