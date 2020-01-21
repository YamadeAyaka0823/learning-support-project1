package com.example.domain;

public class CompanyMember {
	
	/** ID */
	private Integer id;
	/** 氏名 */
	private String name;
	/** かな */
	private String kana;
	/** メールアドレス */
	private String email;
	/** パスワード */
	private String password;
	
	private Integer companyId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public String toString() {
		return "CompanyMember [id=" + id + ", name=" + name + ", kana=" + kana + ", email=" + email + ", password="
				+ password + ", companyId=" + companyId + "]";
	}
	

}
