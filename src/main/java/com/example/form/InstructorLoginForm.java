package com.example.form;

import javax.validation.constraints.NotBlank;

public class InstructorLoginForm {
	
	/** メールアドレス */
	@NotBlank(message="メールアドレスを入力してください")
	private String email;
	/** パスワード */
	@NotBlank(message="パスワードを入力してください")
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "InstructorLoginForm [email=" + email + ", password=" + password + "]";
	}
	
	

}
