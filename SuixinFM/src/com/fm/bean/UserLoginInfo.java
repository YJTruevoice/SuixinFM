package com.fm.bean;

/**
 * 用户登录信息bean
 * 
 * @author
 * 
 */
public class UserLoginInfo {

	private String u_name;
	private String token;

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "UserLoginInfo [u_name=" + u_name + ", token=" + token + "]";
	}

}
