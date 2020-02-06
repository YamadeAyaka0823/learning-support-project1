package com.example.form;

import javax.validation.constraints.NotBlank;

public class InstructorRegisterForm {
	
	/** 名前 */
	@NotBlank(message="名前を入力してください")
	private String name;
	/** かな */
	@NotBlank(message="名前(かな)を入力してください")
	private String kana;
	/** メールアドレス */
	@NotBlank(message="メールアドレスを入力してください")
	private String email;
	/** パスワード */
	@NotBlank(message="パスワードを入力してください")
	private String password;
	/** 所属 */
	@NotBlank(message="所属名を入力してください")
	private String affiliation;
	/** 備考 */
	private String remarks;
	
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
	public String getAffiliation() {
		return affiliation;
	}
	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString() {
		return "InstructorRegisterForm [name=" + name + ", kana=" + kana + ", email=" + email + ", password=" + password
				+ ", affiliation=" + affiliation + ", remarks=" + remarks + "]";
	}
	

}
