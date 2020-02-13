package com.example.form;

public class TrainingUpdateForm {
	
	/** ID */
	private Integer id;
	/** 研修名 */
	private String name;
	/** 開始日 */
	private String startDate;
	/** 終了日 */
	private String endDate;
	/** 講師ID */
	private Integer instructorId;
	/** サブ講師ID1 */
	private Integer subInstructorId1;
	/** サブ講師ID2 */
	private Integer subInstructorId2;
	/** サブ講師ID3 */
	private Integer subInstructorId3;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getInstructorId() {
		return instructorId;
	}
	public void setInstructorId(Integer instructorId) {
		this.instructorId = instructorId;
	}
	public Integer getSubInstructorId1() {
		return subInstructorId1;
	}
	public void setSubInstructorId1(Integer subInstructorId1) {
		this.subInstructorId1 = subInstructorId1;
	}
	public Integer getSubInstructorId2() {
		return subInstructorId2;
	}
	public void setSubInstructorId2(Integer subInstructorId2) {
		this.subInstructorId2 = subInstructorId2;
	}
	public Integer getSubInstructorId3() {
		return subInstructorId3;
	}
	public void setSubInstructorId3(Integer subInstructorId3) {
		this.subInstructorId3 = subInstructorId3;
	}
	
	@Override
	public String toString() {
		return "TrainingUpdateForm [id=" + id + ", name=" + name + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", instructorId=" + instructorId + ", subInstructorId1=" + subInstructorId1 + ", subInstructorId2="
				+ subInstructorId2 + ", subInstructorId3=" + subInstructorId3 + "]";
	}
	
	

}
