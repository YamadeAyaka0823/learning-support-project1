package com.example.domain;

import java.util.List;

public class Student {
	
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
	/** 会社ID */
	private Integer companyId;
	
	private List<Training> trainingList;
	/** 企業 */
	private Company company;
	
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
	public List<Training> getTrainingList() {
		return trainingList;
	}
	public void setTrainingList(List<Training> trainingList) {
		this.trainingList = trainingList;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	@Override
	public String toString() {
		return "Student [id=" + id + ", name=" + name + ", kana=" + kana + ", email=" + email + ", password=" + password
				+ ", companyId=" + companyId + "]";
	}
	
	

}
