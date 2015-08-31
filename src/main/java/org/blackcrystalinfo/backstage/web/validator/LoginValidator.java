package org.blackcrystalinfo.backstage.web.validator;

import org.blackcrystalinfo.backstage.bo.User;
import org.blackcrystalinfo.backstage.busi.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class LoginValidator implements Validator {
	
	@Autowired
	ILoginService loginService;
	
	public boolean supports(Class<?> arg0) {
		return User.class.equals(arg0);
	}

	public void validate(Object arg0, Errors errors) {
//		User user = (User)arg0;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required", "必填");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pwd", "required", "必填");
	}

}
