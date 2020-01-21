package com.example.domain;

import java.time.LocalDate;

public class DailyReport {
	
	/** ID */
	private Integer id;
	/** 日付 */
	private LocalDate date;
	
	private Integer trainingId;
	
	private Integer studentId;
	/** 内容 */
	private String content;
	/** 理解度 */
	private Integer intelligibillity;
	/** 理解度詳細 */
	private String detailIntelligibillity;
	/** 講師について */
	private Integer aboutInstractor;
	/** 質問 */
	private String question;
	/** 研修 */
	private Training training;
	/** 受講生 */
	private Student student;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Integer getTrainingId() {
		return trainingId;
	}
	public void setTrainingId(Integer trainingId) {
		this.trainingId = trainingId;
	}
	public Integer getStudentId() {
		return studentId;
	}
	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getIntelligibillity() {
		return intelligibillity;
	}
	public void setIntelligibillity(Integer intelligibillity) {
		this.intelligibillity = intelligibillity;
	}
	public String getDetailIntelligibillity() {
		return detailIntelligibillity;
	}
	public void setDetailIntelligibillity(String detailIntelligibillity) {
		this.detailIntelligibillity = detailIntelligibillity;
	}
	public Integer getAboutInstractor() {
		return aboutInstractor;
	}
	public void setAboutInstractor(Integer aboutInstractor) {
		this.aboutInstractor = aboutInstractor;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public Training getTraining() {
		return training;
	}
	public void setTraining(Training training) {
		this.training = training;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Override
	public String toString() {
		return "DailyReport [id=" + id + ", date=" + date + ", trainingId=" + trainingId + ", studentId=" + studentId
				+ ", content=" + content + ", intelligibillity=" + intelligibillity + ", detailIntelligibillity="
				+ detailIntelligibillity + ", aboutInstractor=" + aboutInstractor + ", question=" + question
				+ ", training=" + training + ", student=" + student + "]";
	}
	

}
