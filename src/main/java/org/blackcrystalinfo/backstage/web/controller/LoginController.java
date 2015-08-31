package org.blackcrystalinfo.backstage.web.controller;

import javax.validation.Valid;

import org.blackcrystalinfo.backstage.bo.User;
import org.blackcrystalinfo.backstage.busi.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	public LoginController() {
		System.out.println("LoginController.LoginController()");
	}
	@Autowired
	ILoginService loginService;
	
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public void login(Model model){
		model.addAttribute("user",new User());
	}
	
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public String check(@ModelAttribute("user") @Valid User user,BindingResult result) {
		if(result.hasErrors()){
			return "login";
		}
		if(!loginService.validate(user.getName(), user.getPwd())){
			ObjectError o = new ObjectError("",new String[]{"wrong.name.or.pwd"},null,"sth. is going wrong.");
			result.addError(o);
		}
		if(result.hasErrors()){
			return "login";
		}
		return "redirect:/frame";
	}
}
