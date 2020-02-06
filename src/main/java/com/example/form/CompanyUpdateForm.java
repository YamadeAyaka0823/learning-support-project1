package com.example.form;

public class CompanyUpdateForm {
	
	/** ID */
	private String id;
	/** 企業名 */
	private String name;
	/** 企業名(かな) */
	private String kana;
	/** 備考 */
	private String remarks;
	
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
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Override
	public String toString() {
		return "CompanyUpdateForm [id=" + id + ", name=" + name + ", kana=" + kana + ", remarks=" + remarks + "]";
	}
	
	

}
