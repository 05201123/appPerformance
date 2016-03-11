/*
 * @project JHApi
 * @package com.example.jhapi
 * @file RegeditDto.java
 * @version  1.0
 * @author  yourname
 * @time  2013-5-18 上午11:48:11
 * CopyRight:北京金和软件信息技术有限公司 2013-5-18
 */
package com.example.jhapi;

public class AnonymousUserDTO {
	private String Account;
	public String getAccount() {
		return Account;
	}
	public void setAccount(String account) {
		Account = account;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	private String Password;
}
