package org.blackcrystalinfo.backstage.bo;

import org.blackcrystalinfo.backstage.utils.MiscUtils;
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
	public static void main(String[] arg){
		byte[] a = new byte[]{-125, 0, 0, 0, 110, -127, -45, -49, -25, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -126, 0, -16, 6, -49, -45, -127, 104, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -80, -43, -99, 99, -11, -78, 2, 109, 48, 0, 19, 0, 120, 75, 104, -11, 41, 64, -114, 100, 78, 127, 109, 100, 16, 1, 0, 48, 0, 0, 0, 0, 0, 45, 0, 0, 0, 0, 0, 0, 110};
		System.out.printf(MiscUtils.toHex(a));
	}
}

class parent{
	public String f(int a){
		return null;
	}
}
class sub extends parent{
//	public int f(int a){
//		return 0;
//	}
}



