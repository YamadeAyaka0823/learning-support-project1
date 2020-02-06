package com.example.form;

import javax.validation.constraints.NotBlank;

public class CompanyMemberRegisterForm {
	
	/** 氏名 */
	@NotBlank(message="氏名を入力してください")
	private String name;
	/** 氏名(かな) */
	@NotBlank(message="氏名(かな)を入力してください")
	private String kana;
	/** メールアドレス */
	@NotBlank(message="メールアドレスを入力してください")
	private String email;
	/** パスワード */
	@NotBlank(message="パスワードを入力してください")
	private String password;
	/** 確認用パスワード */
	@NotBlank(message="確認用パスワードを入力してください")
	private String confirmPassword;
//	/** 企業ID */
//	@NotBlank
//	private String companyId;
	
//	public Integer getIntCompanyId() {
//		return Integer.parseInt(companyId);
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKana() {
		return kana;
	}

	public void setKana(String kana) {
		this.kana = kana;
	}

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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "CompanyMemberRegisterForm [name=" + name + ", kana=" + kana + ", email=" + email + ", password="
				+ password + ", confirmPassword=" + confirmPassword + "]";
	}

	
	
	
	

}
