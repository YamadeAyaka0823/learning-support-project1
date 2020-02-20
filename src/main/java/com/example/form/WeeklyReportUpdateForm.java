package com.example.form;

import java.util.List;


import com.example.domain.StudentImpression;

public class WeeklyReportUpdateForm {
	
	/** 研修ID **/
	private Integer weeklyReportId;
	/** 授業内容 */
	private String instructorContent;
	/** 研修ID */
	private Integer trainingId;
	/** 受講生所感 */
	private List<StudentImpression> studentImpressionList;
	
	public Integer getWeeklyReportId() {
		return weeklyReportId;
	}
	public void setWeeklyReportId(Integer weeklyReportId) {
		this.weeklyReportId = weeklyReportId;
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
		return "WeeklyReportUpdateForm [weeklyReportId=" + weeklyReportId + ", instructorContent=" + instructorContent
				+ ", trainingId=" + trainingId + ", studentImpressionList=" + studentImpressionList + "]";
	}
	
	
	

}
