package com.example.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class TrainingRegisterForm {
	
	/** 名前 */
	@NotBlank(message="研修コース名を入力してください")
	private String name;
	/** 開始日 */
	@NotBlank(message="開始日を入力してください")
	private String startDate;
	/** 終了日 */
	@NotBlank(message="終了日を入力してください")
	private String endDate;
	/** メイン講師 */
	@NotNull(message="いずれかを選択してください")
	private Integer instructorId;
	/** サブ講師1 */
	@NotNull(message="いずれかを選択してください")
	private Integer subInstructorId1;
	/** サブ講師2 */
	@NotNull(message="いずれかを選択してください")
	private Integer subInstructorId2;
	/** サブ講師3 */
	@NotNull(message="いずれかを選択してください")
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
