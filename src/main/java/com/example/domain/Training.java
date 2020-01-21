package com.example.domain;

import java.time.LocalDate;
import java.util.List;

public class Training {
	
	/** ID */
	private Integer id;
	/** 開始日 */
	private LocalDate startDate;
	/** 終了日 */
	private LocalDate endDate;
	/** 研修名 */
	private String name;
	/** 講師ID */
	private Integer instractorId;
	/** サブ講師ID1 */
	private Integer subInstractorId1;
	/** サブ講師ID2 */
	private Integer subInstractorId2;
	/** サブ講師ID3 */
	private Integer subInstractorId3;
	/** 講師 */
	private Instractor instractor;
	/** 講師1 */
	private Instractor subInstractor1;
	/** 講師2 */
	private Instractor subInstractor2;
	/** 講師3 */
	private Instractor subInstractor3;
	
	private List<Student> studentList;
	
	private List<WeeklyReport> weeklyReportList;

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

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getInstractorId() {
		return instractorId;
	}

	public void setInstractorId(Integer instractorId) {
		this.instractorId = instractorId;
	}

	public Integer getSubInstractorId1() {
		return subInstractorId1;
	}

	public void setSubInstractorId1(Integer subInstractorId1) {
		this.subInstractorId1 = subInstractorId1;
	}

	public Integer getSubInstractorId2() {
		return subInstractorId2;
	}

	public void setSubInstractorId2(Integer subInstractorId2) {
		this.subInstractorId2 = subInstractorId2;
	}

	public Integer getSubInstractorId3() {
		return subInstractorId3;
	}

	public void setSubInstractorId3(Integer subInstractorId3) {
		this.subInstractorId3 = subInstractorId3;
	}

	public Instractor getInstractor() {
		return instractor;
	}

	public void setInstractor(Instractor instractor) {
		this.instractor = instractor;
	}

	public Instractor getSubInstractor1() {
		return subInstractor1;
	}

	public void setSubInstractor1(Instractor subInstractor1) {
		this.subInstractor1 = subInstractor1;
	}

	public Instractor getSubInstractor2() {
		return subInstractor2;
	}

	public void setSubInstractor2(Instractor subInstractor2) {
		this.subInstractor2 = subInstractor2;
	}

	public Instractor getSubInstractor3() {
		return subInstractor3;
	}

	public void setSubInstractor3(Instractor subInstractor3) {
		this.subInstractor3 = subInstractor3;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<WeeklyReport> getWeeklyReportList() {
		return weeklyReportList;
	}

	public void setWeeklyReportList(List<WeeklyReport> weeklyReportList) {
		this.weeklyReportList = weeklyReportList;
	}

	@Override
	public String toString() {
		return "Training [id=" + id + ", startDate=" + startDate + ", endDate=" + endDate + ", name=" + name
				+ ", instractorId=" + instractorId + ", subInstractorId1=" + subInstractorId1 + ", subInstractorId2="
				+ subInstractorId2 + ", subInstractorId3=" + subInstractorId3 + ", studentList=" + studentList + "]";
	}
	
	

}
