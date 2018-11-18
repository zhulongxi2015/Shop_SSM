package cn.itcast.ssm.controller;

import javax.servlet.http.HttpSession;

import cn.itcast.ssm.exception.CustomException;
import cn.itcast.ssm.po.ActiveUser;
import cn.itcast.ssm.service.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

	@Autowired
	private SysService sysService;
	//用户登陆提交方法
	@RequestMapping("/login")
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
	
	//用户退出
	@RequestMapping("/logout")
	public String logout(HttpSession session)throws Exception{
		
		//session失效
		session.invalidate();
		//重定向到商品查询页面
		return "redirect:/first.action";
		
	}
	

}
