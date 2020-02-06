package com.example.form;

import java.util.List;

public class AdminUpdateForm {
	
	/** ID */
	private String id;
	/** 氏名 */
	private String name;
	/** 氏名(かな) */
	private String kana;
	/** メールアドレス */
	private String email;
	/** パスワード */
	private String password;
	/** 確認用パスワード */
	private String confirmPassword;
	
    private String responsibleCompany;
    
	private List<Integer> companyList;
	
	public Integer getIntId() {
		return Integer.parseInt(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getResponsibleCompany() {
		return responsibleCompany;
	}

	public void setResponsibleCompany(String responsibleCompany) {
		this.responsibleCompany = responsibleCompany;
	}

	public List<Integer> getCompanyList() {
		return companyList;
	}

	public void setCompanyList(List<Integer> companyList) {
		this.companyList = companyList;
	}

	@Override
	public String toString() {
		return "AdminUpdateForm [id=" + id + ", name=" + name + ", kana=" + kana + ", email=" + email + ", password="
				+ password + ", confirmPassword=" + confirmPassword + ", responsibleCompany=" + responsibleCompany
				+ ", companyList=" + companyList + "]";
	}
	
	
	
	

}
