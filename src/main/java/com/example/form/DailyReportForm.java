package com.example.form;

import javax.validation.constraints.NotBlank;

public class DailyReportForm {
	
	/** 内容 */
	@NotBlank(message="本日学んだ内容を入力してください")
	private String content;
	/** 理解度 */
	private String intelligibility;
	/** 理解度詳細 */
	@NotBlank(message="理解度詳細を入力してください")
	private String detailIntelligibillity;
	/** 講師について */
	private String aboutInstructor;
	/** 質問 */
	@NotBlank(message="質問等を入力してください")
	private String question;
	/** トレーニングID */
	private Integer trainingId;
	/** 受講生ID */
	private Integer studentId;
	/** 今日の日付 */
	private String formattedDate;
	
	public Integer getIntIntelligibility() {
		return Integer.parseInt(intelligibility);
	}
	
	public Integer getIntAboutInstructor() {
		return Integer.parseInt(aboutInstructor);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIntelligibility() {
		return intelligibility;
	}

	public void setIntelligibility(String intelligibility) {
		this.intelligibility = intelligibility;
	}

	public String getDetailIntelligibillity() {
		return detailIntelligibillity;
	}

	public void setDetailIntelligibillity(String detailIntelligibillity) {
		this.detailIntelligibillity = detailIntelligibillity;
	}

	public String getAboutInstructor() {
		return aboutInstructor;
	}

	public void setAboutInstructor(String aboutInstructor) {
		this.aboutInstructor = aboutInstructor;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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

	public String getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}

	@Override
	public String toString() {
		return "DailyReportForm [content=" + content + ", intelligibility=" + intelligibility
				+ ", detailIntelligibillity=" + detailIntelligibillity + ", aboutInstructor=" + aboutInstructor
				+ ", question=" + question + ", trainingId=" + trainingId + ", studentId=" + studentId
				+ ", formattedDate=" + formattedDate + "]";
	}

	

	

	

	
	
	
	
	

}
