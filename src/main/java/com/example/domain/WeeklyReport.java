package com.example.domain;

import java.time.LocalDate;
import java.util.List;

public class WeeklyReport {
	
	/** ID */
	private Integer id;
	/** 週の初めの日 */
	private LocalDate startDate;
	/** 講師名 */
	private String instractorName;
	/** 内容 */
	private String content;
	
	private List<StudentImpression> studentImpressionList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public String getInstractorName() {
		return instractorName;
	}
	public void setInstractorName(String instractorName) {
		this.instractorName = instractorName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<StudentImpression> getStudentImpressionList() {
		return studentImpressionList;
	}
	public void setStudentImpressionList(List<StudentImpression> studentImpressionList) {
		this.studentImpressionList = studentImpressionList;
	}
	
	@Override
	public String toString() {
		return "WeeklyReport [id=" + id + ", startDate=" + startDate + ", instractorName=" + instractorName
				+ ", content=" + content + "]";
	}
	
	

}
