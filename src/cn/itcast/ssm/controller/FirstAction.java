package cn.itcast.ssm.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import cn.itcast.ssm.po.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class FirstAction {
	//系统首页
	@RequestMapping("/first")
	public String first(Model model)throws Exception{
		Subject subject= SecurityUtils.getSubject();
		ActiveUser activeUser=(ActiveUser) subject.getPrincipal();
		model.addAttribute("activeUser",activeUser);
		return "/first";
	}
	
	//欢迎页面
	@RequestMapping("/welcome")
	public String welcome(Model model)throws Exception{
		
		return "/welcome";
		
	}
}	
