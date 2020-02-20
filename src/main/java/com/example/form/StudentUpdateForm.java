package com.example.form;

public class StudentUpdateForm {
	
	/** 受講生ID */
	private Integer studentId;
	/** 名前 */
	private String name;
	/** かな */
	private String kana;
	/** メールアドレス */
	private String email;
	
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
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
	
	@Override
	public String toString() {
		return "StudentUpdateForm [studentId=" + studentId + ", name=" + name + ", kana=" + kana + ", email=" + email
				+ "]";
	}
	

}
