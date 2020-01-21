package com.example.domain;

import java.util.List;

public class Admin {
	
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
	/** 全ての企業情報が見れるか? */
	private Boolean canShowAllCompany;
	
	private List<Company> companyList;
	
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
	public Boolean getCanShowAllCompany() {
		return canShowAllCompany;
	}
	public void setCanShowAllCompany(Boolean canShowAllCompany) {
		this.canShowAllCompany = canShowAllCompany;
	}
	public List<Company> getCompanyList() {
		return companyList;
	}
	public void setCompanyList(List<Company> companyList) {
		this.companyList = companyList;
	}
	
	@Override
	public String toString() {
		return "Admin [id=" + id + ", name=" + name + ", kana=" + kana + ", email=" + email + ", password=" + password
				+ ", canShowAllCompany=" + canShowAllCompany + ", companyList=" + companyList + "]";
	}
	

}
