package com.example.domain;

import java.util.Date;
import java.util.List;

public class WeeklyReport {
	
	/** ID */
	private Integer id;
	/** 週の初めの日 */
	private Date startDate;
	/** 講師名 */
	private String instructorName;
	/** 内容 */
	private String content;
	/** 研修ID */
	private Integer trainingId;
	
	private List<StudentImpression> studentImpressionList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		return "WeeklyReport [id=" + id + ", startDate=" + startDate + ", instructorName=" + instructorName
				+ ", content=" + content + ", trainingId=" + trainingId + ", studentImpressionList="
				+ studentImpressionList + "]";
	}

	

	
	
	
	
	

}
