package com.example.domain;

import java.util.Date;

public class AdminPasswordResetToken {
	
	/** トークンが24時間有効 */
	private static final int EXPIRATION = 60 * 24;
	/** ID */
	private Long id;
	/** トークン */
	private String token;
	
	private Admin admin;
	
	private Date expiryDate;
	
	public AdminPasswordResetToken() {
		
	}
	
	public AdminPasswordResetToken(Long id, String token, Admin admin, Date expiryDate) {
		super();
		this.id = id;
		this.token = token;
		this.admin = admin;
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
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
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
		return "AdminPasswordResetToken [id=" + id + ", token=" + token + ", admin=" + admin + ", expiryDate="
				+ expiryDate + "]";
	}
	

}
