package com.example.form;


public class StudentImpressionUpdateForm {
	
	/** ID */
//	private Integer id;
	/** 内容 */
	private String studentContent;
	/** 週報ID */
	private Integer weeklyReportId;
	
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
	public String getStudentContent() {
		return studentContent;
	}
	public void setStudentContent(String studentContent) {
		this.studentContent = studentContent;
	}
	public Integer getWeeklyReportId() {
		return weeklyReportId;
	}
	public void setWeeklyReportId(Integer weeklyReportId) {
		this.weeklyReportId = weeklyReportId;
	}
	@Override
	public String toString() {
		return "StudentImpressionUpdateForm [studentContent=" + studentContent + ", weeklyReportId=" + weeklyReportId
				+ "]";
	}
	
	
	
	
	

}
