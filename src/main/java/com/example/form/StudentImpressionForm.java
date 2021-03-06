package com.example.form;

import javax.validation.constraints.NotBlank;

public class StudentImpressionForm {
	
	/** 内容 */
	@NotBlank(message="内容を入力してください")
	private String studentContent;
	/** 週報ID */
	private Integer weeklyReportId;
	/** 受講生名 */
	private String studentName;
	
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
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	

	
	
	

}
