package com.example.form;


public class DailyReportForm {
	
	/** 内容 */
	private String content;
	/** 理解度 */
	private String intelligibility;
	/** 理解度詳細 */
	private String detailIntelligibillity;
	/** 講師について */
	private String aboutInstructor;
	/** 質問 */
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
