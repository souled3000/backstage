package org.blackcrystalinfo.backstage.bo;

import org.hibernate.validator.constraints.NotEmpty;

public class User {
	// @NotEmpty(message="登录名不可为空")
	@NotEmpty(message = "{required.name}")
	private String name;
	// @NotEmpty(message="密码不可为空")
	@NotEmpty(message = "{required.pwd}")
	private String pwd;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
