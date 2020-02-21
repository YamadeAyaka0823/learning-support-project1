package com.example.domain;

import java.util.Date;

public class CompanyMemberPasswordResetToken {
	
	/** トークンが24時間有効 */
	private static final int EXPIRATION = 60 * 24;
	/** ID */
	private Long id;
	/** トークン */
	private String token;
	
	private CompanyMember companyMember;
	
	private Date expiryDate;
	
	public CompanyMemberPasswordResetToken() {
		
	}
	
	public CompanyMemberPasswordResetToken(Long id, String token, CompanyMember companyMember, Date expiryDate) {
		super();
		this.id = id;
		this.token = token;
		this.companyMember = companyMember;
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
	public CompanyMember getCompanyMember() {
		return companyMember;
	}
	public void setCompanyMember(CompanyMember companyMember) {
		this.companyMember = companyMember;
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
		return "CompanyMemberPasswordResetToken [id=" + id + ", token=" + token + ", companyMember=" + companyMember
				+ ", expiryDate=" + expiryDate + "]";
	}
	

}
