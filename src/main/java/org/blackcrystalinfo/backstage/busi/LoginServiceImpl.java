package org.blackcrystalinfo.backstage.busi;

import org.springframework.stereotype.Repository;

@Repository
public class LoginServiceImpl implements ILoginService {
	private String name;
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
	public boolean validate(String name,String pwd){
		if(!name.equals(this.name)){
			return false;
		}
		if(!pwd.equals(this.pwd)){
			return false;
		}
		return true;
	}
}
