package com.example.domain;

public class StudentImpression {
	
	/** ID */
	private Integer id;
	/** 週報ID */
	private Integer weeklyReportId;
	/** 受講生名 */
	private String studentName;
	/** 内容 */
	private String content;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "StudentImpression [id=" + id + ", weeklyReportId=" + weeklyReportId + ", studentName=" + studentName
				+ ", content=" + content + "]";
	}
	

}
