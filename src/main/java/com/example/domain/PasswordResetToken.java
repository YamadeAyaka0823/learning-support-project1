package com.example.domain;

import java.util.Date;

public class PasswordResetToken {
	
	/** トークンが24時間有効 */
	private static final int EXPIRATION = 60 * 24;
	/** ID */
	private Long id;
	/** トークン */
	private String token;
	
	private Student student;
	
	private Date expiryDate;
	
	public PasswordResetToken() {
		
	}

	public PasswordResetToken(String token, Student student) {
		super();
		this.token = token;
		this.student = student;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public static int getExpiration() {
		return EXPIRATION;
	}

	@Override
	public String toString() {
		return "PasswordResetToken [id=" + id + ", token=" + token + ", student=" + student + ", expiryDate="
				+ expiryDate + "]";
	}
	
	

}
