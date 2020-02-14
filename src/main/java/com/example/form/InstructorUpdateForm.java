package com.example.form;

public class InstructorUpdateForm {
	
	/** ID */
	private String id;
	/** 名前 */
	private String name;
	/** 名前(かな) */
	private String kana;
	/** メールアドレス */
	private String email;
	/** 備考 */
	private String remarks;
	/** 所属 */
	private String affiliation;
	
	public Integer getIntId() {
		return Integer.parseInt(id);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKana() {
		return kana;
	}

	public void setKana(String kana) {
		this.kana = kana;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	@Override
	public String toString() {
		return "InstructorUpdateForm [id=" + id + ", name=" + name + ", kana=" + kana + ", email=" + email
				+ ", remarks=" + remarks + ", affiliation=" + affiliation + "]";
	}
	
	
	
	

}
