package org.blackcrystalinfo.backstage.bo;

public class IntStr {
	private int code;
	private String name;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public IntStr() {
		
	}
	
	public IntStr(int code,String name){
		this.code=code;
		this.name=name;
	}
}
