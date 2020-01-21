package com.example.domain;

public class Instractor {
	
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
	/** 所属 */
	private String affilication;
	/** 備考 */
	private String remarks;
	
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
	public String getAffilication() {
		return affilication;
	}
	public void setAffilication(String affilication) {
		this.affilication = affilication;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString() {
		return "Instractor [id=" + id + ", name=" + name + ", kana=" + kana + ", email=" + email + ", password="
				+ password + ", affilication=" + affilication + ", remarks=" + remarks + "]";
	}
	
	

}
