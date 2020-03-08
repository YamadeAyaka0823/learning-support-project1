package com.example.form;

import javax.validation.constraints.NotBlank;

public class CompanyRegisterForm {
	
	/** 企業名 */
//	@NotBlank(message="企業名を入力してください")
	private String name;
	/** 企業名(かな) */
//	@NotBlank(message="企業名(かな)を入力してください")
	private String kana;
	/** 備考 */
//	@NotBlank(message="備考を入力してください")
	private String remarks;
	
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
		return "CompanyRegisterForm [name=" + name + ", kana=" + kana + ", remarks=" + remarks + "]";
	}
	
	

}
