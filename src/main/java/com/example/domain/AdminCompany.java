package com.example.domain;

public class AdminCompany {
	
	/** 管理者ID */
	private Integer adminId;
	/** 企業ID */
	private Integer companyId;
	
	public Integer getAdminId() {
		return adminId;
	}
	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public String toString() {
		return "AdminCompany [adminId=" + adminId + ", companyId=" + companyId + "]";
	}
	
	

}
