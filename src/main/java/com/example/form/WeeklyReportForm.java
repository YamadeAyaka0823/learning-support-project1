package com.example.form;

import java.util.List;

import com.example.domain.StudentImpression;

public class WeeklyReportForm {
	
	/** 週の始めの日 */
	private String startDate;
	/** 講師の名前 */
	private String instructorName;
	/** 授業内容 */
	private String instructorContent;
	/** 研修ID */
	private Integer trainingId;
	/** 受講生所感 */
	private List<StudentImpression> studentImpressionList;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getInstructorName() {
		return instructorName;
	}
	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}
	public String getInstructorContent() {
		return instructorContent;
	}
	public void setInstructorContent(String instructorContent) {
		this.instructorContent = instructorContent;
	}
	public Integer getTrainingId() {
		return trainingId;
	}
	public void setTrainingId(Integer trainingId) {
		this.trainingId = trainingId;
	}
	public List<StudentImpression> getStudentImpressionList() {
		return studentImpressionList;
	}
	public void setStudentImpressionList(List<StudentImpression> studentImpressionList) {
		this.studentImpressionList = studentImpressionList;
	}
	@Override
	public String toString() {
		return "WeeklyReportForm [startDate=" + startDate + ", instructorName=" + instructorName
				+ ", instructorContent=" + instructorContent + ", trainingId=" + trainingId + ", studentImpressionList="
				+ studentImpressionList + "]";
	}
	
	
	
	
	
	
	
	
	

}
