package com.example.form;


public class TrainingRegisterForm {
	
	/** 名前 */
	private String name;
	/** 開始日 */
	private String startDate;
	/** 終了日 */
	private String endDate;
	/** メイン講師 */
	private Integer instructorId;
	/** サブ講師1 */
	private Integer subInstructorId1;
	/** サブ講師2 */
	private Integer subInstructorId2;
	/** サブ講師3 */
	private Integer subInstructorId3;
	
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
		return "TrainingRegisterForm [name=" + name + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", instructorId=" + instructorId + ", subInstructorId1=" + subInstructorId1 + ", subInstructorId2="
				+ subInstructorId2 + ", subInstructorId3=" + subInstructorId3 + "]";
	}
	
	
	
	

}
