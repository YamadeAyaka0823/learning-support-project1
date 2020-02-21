package com.example.domain;

import java.util.Date;

public class InstructorPasswordResetToken {
	
	/** トークンが24時間有効 */
	private static final int EXPIRATION = 60 * 24;
	/** ID */
	private Long id;
	/** トークン */
	private String token;
	
	private Instructor instructor;
	
	private Date expiryDate;
	
	public InstructorPasswordResetToken() {
		
	}
	
	public InstructorPasswordResetToken(Long id, String token, Instructor instructor, Date expiryDate) {
		super();
		this.id = id;
		this.token = token;
		this.instructor = instructor;
		this.expiryDate = expiryDate;
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
	public Instructor getInstructor() {
		return instructor;
	}
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
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
		return "InstructorPasswordResetToken [id=" + id + ", token=" + token + ", instructor=" + instructor
				+ ", expiryDate=" + expiryDate + "]";
	}
	

}
